DELIMITER $$

CREATE PROCEDURE sp_board_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_title VARCHAR(255),
    IN p_content TEXT,
    IN p_writer_id BIGINT,
    IN p_like_count INT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_writer_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_title IS NULL OR TRIM(p_title) = '' THEN
            SET p_result_message = '제목은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_content IS NULL OR TRIM(p_content) = '' THEN
            SET p_result_message = '내용은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_writer_id IS NULL THEN
            SET p_result_message = '작성자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_writer_exists FROM member WHERE id = p_writer_id;
            IF v_writer_exists = 0 THEN
                SET p_result_message = '존재하지 않는 작성자 ID입니다.';
                ROLLBACK;
            ELSE
                INSERT INTO board (title, content, writer_id, like_count, created_at, updated_at)
                VALUES (p_title, p_content, p_writer_id, IFNULL(p_like_count, 0), NOW(), NOW());
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('게시글이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                COMMIT;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 게시글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM board WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 게시글이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_writer_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_writer_exists FROM member WHERE id = p_writer_id;
                    IF v_writer_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 작성자 ID입니다.';
                        ROLLBACK;
                    ELSE
                        UPDATE board
                        SET title = IFNULL(NULLIF(TRIM(p_title), ''), title),
                            content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                            writer_id = IFNULL(p_writer_id, writer_id),
                            like_count = IFNULL(p_like_count, like_count),
                            updated_at = NOW()
                        WHERE id = p_id;
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('게시글이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                        COMMIT;
                    END IF;
                ELSE
                    UPDATE board
                    SET title = IFNULL(NULLIF(TRIM(p_title), ''), title),
                        content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                        like_count = IFNULL(p_like_count, like_count),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('게시글이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 게시글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM board WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 게시글이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM board WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('게시글이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_product_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_title VARCHAR(255),
    IN p_content TEXT,
    IN p_location VARCHAR(50),
    IN p_price INT,
    IN p_view_count INT,
    IN p_like_count INT,
    IN p_product_status VARCHAR(20),
    IN p_category_id BIGINT,
    IN p_member_id BIGINT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_category_exists INT DEFAULT 0;
    DECLARE v_member_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_title IS NULL OR TRIM(p_title) = '' THEN
            SET p_result_message = '상품명은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_content IS NULL OR TRIM(p_content) = '' THEN
            SET p_result_message = '상품 설명은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_location IS NULL OR TRIM(p_location) = '' THEN
            SET p_result_message = '거래 장소는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_price IS NULL OR p_price < 0 THEN
            SET p_result_message = '상품 가격은 0 이상이어야 합니다.';
            ROLLBACK;
        ELSEIF p_category_id IS NULL THEN
            SET p_result_message = '카테고리 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_member_id IS NULL THEN
            SET p_result_message = '판매자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_category_exists FROM category WHERE id = p_category_id;
            IF v_category_exists = 0 THEN
                SET p_result_message = '존재하지 않는 카테고리 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                IF v_member_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 판매자 ID입니다.';
                    ROLLBACK;
                ELSE
                    INSERT INTO product (title, content, location, price, view_count, like_count, product_status, category_id, member_id, created_at, updated_at)
                    VALUES (p_title, p_content, p_location, p_price, IFNULL(p_view_count, 0), IFNULL(p_like_count, 0), IFNULL(p_product_status, 'WAITING'), p_category_id, p_member_id, NOW(), NOW());
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('상품이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 상품 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM product WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 상품이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_category_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_category_exists FROM category WHERE id = p_category_id;
                    IF v_category_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 카테고리 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_member_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                            IF v_member_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 판매자 ID입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE product
                                SET title = IFNULL(NULLIF(TRIM(p_title), ''), title),
                                    content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                                    location = IFNULL(NULLIF(TRIM(p_location), ''), location),
                                    price = IFNULL(p_price, price),
                                    view_count = IFNULL(p_view_count, view_count),
                                    like_count = IFNULL(p_like_count, like_count),
                                    product_status = IFNULL(p_product_status, product_status),
                                    category_id = IFNULL(p_category_id, category_id),
                                    member_id = IFNULL(p_member_id, member_id),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('상품이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE product
                            SET title = IFNULL(NULLIF(TRIM(p_title), ''), title),
                                content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                                location = IFNULL(NULLIF(TRIM(p_location), ''), location),
                                price = IFNULL(p_price, price),
                                view_count = IFNULL(p_view_count, view_count),
                                like_count = IFNULL(p_like_count, like_count),
                                product_status = IFNULL(p_product_status, product_status),
                                category_id = IFNULL(p_category_id, category_id),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('상품이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE product
                    SET title = IFNULL(NULLIF(TRIM(p_title), ''), title),
                        content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                        location = IFNULL(NULLIF(TRIM(p_location), ''), location),
                        price = IFNULL(p_price, price),
                        view_count = IFNULL(p_view_count, view_count),
                        like_count = IFNULL(p_like_count, like_count),
                        product_status = IFNULL(p_product_status, product_status),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('상품이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 상품 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM product WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 상품이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM product WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('상품이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_coupon_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_name VARCHAR(255),
    IN p_content TEXT,
    IN p_can_use_alone BOOLEAN,
    IN p_is_discount_percentage BOOLEAN,
    IN p_amount INT,
    IN p_price INT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_name IS NULL OR TRIM(p_name) = '' THEN
            SET p_result_message = '쿠폰명은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_content IS NULL OR TRIM(p_content) = '' THEN
            SET p_result_message = '쿠폰 설명은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_amount IS NULL OR p_amount < 0 THEN
            SET p_result_message = '할인 금액/율은 0 이상이어야 합니다.';
            ROLLBACK;
        ELSEIF p_is_discount_percentage = TRUE AND (p_amount < 1 OR p_amount > 100) THEN
            SET p_result_message = '할인율은 1~100 사이여야 합니다.';
            ROLLBACK;
        ELSE
            INSERT INTO coupon (name, content, can_use_alone, is_discount_percentage, amount, price, created_at, updated_at)
            VALUES (p_name, p_content, IFNULL(p_can_use_alone, FALSE), IFNULL(p_is_discount_percentage, FALSE), p_amount, IFNULL(p_price, 0), NOW(), NOW());
            
            SET p_affected_rows = ROW_COUNT();
            SET p_result_message = CONCAT('쿠폰이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
            COMMIT;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 쿠폰 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM coupon WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 쿠폰이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_amount IS NOT NULL AND p_is_discount_percentage = TRUE AND (p_amount < 1 OR p_amount > 100) THEN
                    SET p_result_message = '할인율은 1~100 사이여야 합니다.';
                    ROLLBACK;
                ELSE
                    UPDATE coupon
                    SET name = IFNULL(NULLIF(TRIM(p_name), ''), name),
                        content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                        can_use_alone = IFNULL(p_can_use_alone, can_use_alone),
                        is_discount_percentage = IFNULL(p_is_discount_percentage, is_discount_percentage),
                        amount = IFNULL(p_amount, amount),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('쿠폰이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 쿠폰 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM coupon WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 쿠폰이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM coupon WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('쿠폰이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_member_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_email VARCHAR(255),
    IN p_password VARCHAR(255),
    IN p_nickname VARCHAR(50),
    IN p_member_role VARCHAR(20),
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_email_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_email IS NULL OR TRIM(p_email) = '' THEN
            SET p_result_message = '이메일은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_password IS NULL OR TRIM(p_password) = '' THEN
            SET p_result_message = '비밀번호는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_nickname IS NULL OR TRIM(p_nickname) = '' THEN
            SET p_result_message = '닉네임은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_email_exists FROM member WHERE email = p_email;
            IF v_email_exists > 0 THEN
                SET p_result_message = '이미 존재하는 이메일입니다.';
                ROLLBACK;
            ELSE
                INSERT INTO member (email, password, nickname, member_role, created_at, updated_at)
                VALUES (p_email, p_password, p_nickname, IFNULL(p_member_role, 'MEMBER'), NOW(), NOW());
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('회원이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                COMMIT;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM member WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 회원이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_email IS NOT NULL AND TRIM(p_email) != '' THEN
                    SELECT COUNT(*) INTO v_email_exists FROM member WHERE email = p_email AND id != p_id;
                    IF v_email_exists > 0 THEN
                        SET p_result_message = '이미 존재하는 이메일입니다.';
                        ROLLBACK;
                    ELSE
                        UPDATE member
                        SET email = IFNULL(NULLIF(TRIM(p_email), ''), email),
                            password = IFNULL(NULLIF(TRIM(p_password), ''), password),
                            nickname = IFNULL(NULLIF(TRIM(p_nickname), ''), nickname),
                            member_role = IFNULL(p_member_role, member_role),
                            updated_at = NOW()
                        WHERE id = p_id;
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('회원이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                        COMMIT;
                    END IF;
                ELSE
                    UPDATE member
                    SET password = IFNULL(NULLIF(TRIM(p_password), ''), password),
                        nickname = IFNULL(NULLIF(TRIM(p_nickname), ''), nickname),
                        member_role = IFNULL(p_member_role, member_role),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('회원이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM member WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 회원이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM member WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('회원이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_comment_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_content TEXT,
    IN p_writer_id BIGINT,
    IN p_board_id BIGINT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_writer_exists INT DEFAULT 0;
    DECLARE v_board_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_content IS NULL OR TRIM(p_content) = '' THEN
            SET p_result_message = '댓글 내용은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_writer_id IS NULL THEN
            SET p_result_message = '작성자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_board_id IS NULL THEN
            SET p_result_message = '게시글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_writer_exists FROM member WHERE id = p_writer_id;
            IF v_writer_exists = 0 THEN
                SET p_result_message = '존재하지 않는 작성자 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_board_exists FROM board WHERE id = p_board_id;
                IF v_board_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 게시글 ID입니다.';
                    ROLLBACK;
                ELSE
                    INSERT INTO comment (content, writer_id, board_id, created_at, updated_at)
                    VALUES (p_content, p_writer_id, p_board_id, NOW(), NOW());
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('댓글이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 댓글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM comment WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 댓글이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_writer_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_writer_exists FROM member WHERE id = p_writer_id;
                    IF v_writer_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 작성자 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_board_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_board_exists FROM board WHERE id = p_board_id;
                            IF v_board_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 게시글 ID입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE comment
                                SET content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                                    writer_id = IFNULL(p_writer_id, writer_id),
                                    board_id = IFNULL(p_board_id, board_id),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('댓글이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE comment
                            SET content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                                writer_id = IFNULL(p_writer_id, writer_id),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('댓글이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE comment
                    SET content = IFNULL(NULLIF(TRIM(p_content), ''), content),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('댓글이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 댓글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM comment WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 댓글이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM comment WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('댓글이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_category_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_name VARCHAR(50),
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_name IS NULL OR TRIM(p_name) = '' THEN
            SET p_result_message = '카테고리명은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            INSERT INTO category (name, created_at, updated_at)
            VALUES (p_name, NOW(), NOW());
            
            SET p_affected_rows = ROW_COUNT();
            SET p_result_message = CONCAT('카테고리가 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
            COMMIT;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 카테고리 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM category WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 카테고리가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                UPDATE category
                SET name = IFNULL(NULLIF(TRIM(p_name), ''), name),
                    updated_at = NOW()
                WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('카테고리가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 카테고리 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM category WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 카테고리가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM category WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('카테고리가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_product_like_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_member_id BIGINT,
    IN p_product_id BIGINT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_member_exists INT DEFAULT 0;
    DECLARE v_product_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_member_id IS NULL THEN
            SET p_result_message = '회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_product_id IS NULL THEN
            SET p_result_message = '상품 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
            IF v_member_exists = 0 THEN
                SET p_result_message = '존재하지 않는 회원 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
                IF v_product_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 상품 ID입니다.';
                    ROLLBACK;
                ELSE
                    SELECT COUNT(*) INTO v_count FROM product_like WHERE member_id = p_member_id AND product_id = p_product_id;
                    IF v_count > 0 THEN
                        SET p_result_message = '이미 좋아요가 등록되어 있습니다.';
                        ROLLBACK;
                    ELSE
                        INSERT INTO product_like (member_id, product_id, created_at, updated_at)
                        VALUES (p_member_id, p_product_id, NOW(), NOW());
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('상품 좋아요가 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 상품 좋아요 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM product_like WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 상품 좋아요가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_member_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                    IF v_member_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 회원 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_product_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
                            IF v_product_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 상품 ID입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE product_like
                                SET member_id = IFNULL(p_member_id, member_id),
                                    product_id = IFNULL(p_product_id, product_id),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('상품 좋아요가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE product_like
                            SET member_id = IFNULL(p_member_id, member_id),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('상품 좋아요가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE product_like
                    SET product_id = IFNULL(p_product_id, product_id),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('상품 좋아요가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM product_like WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 상품 좋아요가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM product_like WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('상품 좋아요가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSEIF p_member_id IS NOT NULL AND p_product_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM product_like WHERE member_id = p_member_id AND product_id = p_product_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 상품 좋아요가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM product_like WHERE member_id = p_member_id AND product_id = p_product_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('상품 좋아요가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSE
            SET p_result_message = '삭제를 위해서는 ID 또는 member_id와 product_id가 필요합니다.';
            ROLLBACK;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_member_coupon_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_member_id BIGINT,
    IN p_coupon_id BIGINT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_member_exists INT DEFAULT 0;
    DECLARE v_coupon_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_member_id IS NULL THEN
            SET p_result_message = '회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_coupon_id IS NULL THEN
            SET p_result_message = '쿠폰 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
            IF v_member_exists = 0 THEN
                SET p_result_message = '존재하지 않는 회원 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_coupon_exists FROM coupon WHERE id = p_coupon_id;
                IF v_coupon_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 쿠폰 ID입니다.';
                    ROLLBACK;
                ELSE
                    SELECT COUNT(*) INTO v_count FROM member_coupon WHERE member_id = p_member_id AND coupon_id = p_coupon_id;
                    IF v_count > 0 THEN
                        SET p_result_message = '이미 보유한 쿠폰입니다.';
                        ROLLBACK;
                    ELSE
                        INSERT INTO member_coupon (member_id, coupon_id, created_at, updated_at)
                        VALUES (p_member_id, p_coupon_id, NOW(), NOW());
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 회원 쿠폰 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM member_coupon WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 회원 쿠폰이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_member_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                    IF v_member_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 회원 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_coupon_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_coupon_exists FROM coupon WHERE id = p_coupon_id;
                            IF v_coupon_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 쿠폰 ID입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE member_coupon
                                SET member_id = IFNULL(p_member_id, member_id),
                                    coupon_id = IFNULL(p_coupon_id, coupon_id),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE member_coupon
                            SET member_id = IFNULL(p_member_id, member_id),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE member_coupon
                    SET coupon_id = IFNULL(p_coupon_id, coupon_id),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM member_coupon WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 회원 쿠폰이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM member_coupon WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSEIF p_member_id IS NOT NULL AND p_coupon_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM member_coupon WHERE member_id = p_member_id AND coupon_id = p_coupon_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 회원 쿠폰이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM member_coupon WHERE member_id = p_member_id AND coupon_id = p_coupon_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('회원 쿠폰이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSE
            SET p_result_message = '삭제를 위해서는 ID 또는 member_id와 coupon_id가 필요합니다.';
            ROLLBACK;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_trade_history_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_buyer_id BIGINT,
    IN p_seller_id BIGINT,
    IN p_product_id BIGINT,
    IN p_product_origin_price INT,
    IN p_product_discount_price INT,
    IN p_using_coupon_ids VARCHAR(500),
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_buyer_exists INT DEFAULT 0;
    DECLARE v_seller_exists INT DEFAULT 0;
    DECLARE v_product_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_buyer_id IS NULL THEN
            SET p_result_message = '구매자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_seller_id IS NULL THEN
            SET p_result_message = '판매자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_product_id IS NULL THEN
            SET p_result_message = '상품 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_product_origin_price IS NULL OR p_product_origin_price < 0 THEN
            SET p_result_message = '상품 원가는 0 이상이어야 합니다.';
            ROLLBACK;
        ELSEIF p_product_discount_price IS NULL OR p_product_discount_price < 0 THEN
            SET p_result_message = '할인 적용 후 가격은 0 이상이어야 합니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_buyer_exists FROM member WHERE id = p_buyer_id;
            IF v_buyer_exists = 0 THEN
                SET p_result_message = '존재하지 않는 구매자 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_seller_exists FROM member WHERE id = p_seller_id;
                IF v_seller_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 판매자 ID입니다.';
                    ROLLBACK;
                ELSE
                    SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
                    IF v_product_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 상품 ID입니다.';
                        ROLLBACK;
                    ELSE
                        INSERT INTO trade_history (buyer_id, seller_id, product_id, product_origin_price, product_discount_price, using_coupon_ids, created_at, updated_at)
                        VALUES (p_buyer_id, p_seller_id, p_product_id, p_product_origin_price, p_product_discount_price, p_using_coupon_ids, NOW(), NOW());
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('거래 내역이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 거래 내역 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM trade_history WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 거래 내역이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_buyer_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_buyer_exists FROM member WHERE id = p_buyer_id;
                    IF v_buyer_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 구매자 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_seller_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_seller_exists FROM member WHERE id = p_seller_id;
                            IF v_seller_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 판매자 ID입니다.';
                                ROLLBACK;
                            ELSE
                                IF p_product_id IS NOT NULL THEN
                                    SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
                                    IF v_product_exists = 0 THEN
                                        SET p_result_message = '존재하지 않는 상품 ID입니다.';
                                        ROLLBACK;
                                    ELSE
                                        UPDATE trade_history
                                        SET buyer_id = IFNULL(p_buyer_id, buyer_id),
                                            seller_id = IFNULL(p_seller_id, seller_id),
                                            product_id = IFNULL(p_product_id, product_id),
                                            product_origin_price = IFNULL(p_product_origin_price, product_origin_price),
                                            product_discount_price = IFNULL(p_product_discount_price, product_discount_price),
                                            using_coupon_ids = IFNULL(p_using_coupon_ids, using_coupon_ids),
                                            updated_at = NOW()
                                        WHERE id = p_id;
                                        
                                        SET p_affected_rows = ROW_COUNT();
                                        SET p_result_message = CONCAT('거래 내역이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                        COMMIT;
                                    END IF;
                                ELSE
                                    UPDATE trade_history
                                    SET buyer_id = IFNULL(p_buyer_id, buyer_id),
                                        seller_id = IFNULL(p_seller_id, seller_id),
                                        product_origin_price = IFNULL(p_product_origin_price, product_origin_price),
                                        product_discount_price = IFNULL(p_product_discount_price, product_discount_price),
                                        using_coupon_ids = IFNULL(p_using_coupon_ids, using_coupon_ids),
                                        updated_at = NOW()
                                    WHERE id = p_id;
                                    
                                    SET p_affected_rows = ROW_COUNT();
                                    SET p_result_message = CONCAT('거래 내역이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                    COMMIT;
                                END IF;
                            END IF;
                        ELSE
                            UPDATE trade_history
                            SET buyer_id = IFNULL(p_buyer_id, buyer_id),
                                product_id = IFNULL(p_product_id, product_id),
                                product_origin_price = IFNULL(p_product_origin_price, product_origin_price),
                                product_discount_price = IFNULL(p_product_discount_price, product_discount_price),
                                using_coupon_ids = IFNULL(p_using_coupon_ids, using_coupon_ids),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('거래 내역이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE trade_history
                    SET seller_id = IFNULL(p_seller_id, seller_id),
                        product_id = IFNULL(p_product_id, product_id),
                        product_origin_price = IFNULL(p_product_origin_price, product_origin_price),
                        product_discount_price = IFNULL(p_product_discount_price, product_discount_price),
                        using_coupon_ids = IFNULL(p_using_coupon_ids, using_coupon_ids),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('거래 내역이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 거래 내역 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM trade_history WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 거래 내역이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM trade_history WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('거래 내역이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_voucher_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_coupon_id BIGINT,
    IN p_description VARCHAR(255),
    IN p_voucher_number VARCHAR(50),
    IN p_is_public BOOLEAN,
    IN p_is_used BOOLEAN,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_coupon_exists INT DEFAULT 0;
    DECLARE v_voucher_number_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_coupon_id IS NULL THEN
            SET p_result_message = '쿠폰 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_voucher_number IS NULL OR TRIM(p_voucher_number) = '' THEN
            SET p_result_message = '바우처 번호는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_coupon_exists FROM coupon WHERE id = p_coupon_id;
            IF v_coupon_exists = 0 THEN
                SET p_result_message = '존재하지 않는 쿠폰 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_voucher_number_exists FROM voucher WHERE voucher_number = p_voucher_number;
                IF v_voucher_number_exists > 0 THEN
                    SET p_result_message = '이미 존재하는 바우처 번호입니다.';
                    ROLLBACK;
                ELSE
                    INSERT INTO voucher (coupon_id, description, voucher_number, is_public, is_used, created_at, updated_at)
                    VALUES (p_coupon_id, p_description, p_voucher_number, IFNULL(p_is_public, FALSE), IFNULL(p_is_used, FALSE), NOW(), NOW());
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('바우처가 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 바우처 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM voucher WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 바우처가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_coupon_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_coupon_exists FROM coupon WHERE id = p_coupon_id;
                    IF v_coupon_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 쿠폰 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_voucher_number IS NOT NULL AND TRIM(p_voucher_number) != '' THEN
                            SELECT COUNT(*) INTO v_voucher_number_exists FROM voucher WHERE voucher_number = p_voucher_number AND id != p_id;
                            IF v_voucher_number_exists > 0 THEN
                                SET p_result_message = '이미 존재하는 바우처 번호입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE voucher
                                SET coupon_id = IFNULL(p_coupon_id, coupon_id),
                                    description = IFNULL(p_description, description),
                                    voucher_number = IFNULL(NULLIF(TRIM(p_voucher_number), ''), voucher_number),
                                    is_public = IFNULL(p_is_public, is_public),
                                    is_used = IFNULL(p_is_used, is_used),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('바우처가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE voucher
                            SET coupon_id = IFNULL(p_coupon_id, coupon_id),
                                description = IFNULL(p_description, description),
                                is_public = IFNULL(p_is_public, is_public),
                                is_used = IFNULL(p_is_used, is_used),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('바우처가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    IF p_voucher_number IS NOT NULL AND TRIM(p_voucher_number) != '' THEN
                        SELECT COUNT(*) INTO v_voucher_number_exists FROM voucher WHERE voucher_number = p_voucher_number AND id != p_id;
                        IF v_voucher_number_exists > 0 THEN
                            SET p_result_message = '이미 존재하는 바우처 번호입니다.';
                            ROLLBACK;
                        ELSE
                            UPDATE voucher
                            SET description = IFNULL(p_description, description),
                                voucher_number = IFNULL(NULLIF(TRIM(p_voucher_number), ''), voucher_number),
                                is_public = IFNULL(p_is_public, is_public),
                                is_used = IFNULL(p_is_used, is_used),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('바우처가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    ELSE
                        UPDATE voucher
                        SET description = IFNULL(p_description, description),
                            is_public = IFNULL(p_is_public, is_public),
                            is_used = IFNULL(p_is_used, is_used),
                            updated_at = NOW()
                        WHERE id = p_id;
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('바우처가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 바우처 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM voucher WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 바우처가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM voucher WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('바우처가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE PROCEDURE sp_like_storage_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_board_id BIGINT,
    IN p_member_id BIGINT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_board_exists INT DEFAULT 0;
    DECLARE v_member_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_board_id IS NULL THEN
            SET p_result_message = '게시글 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_member_id IS NULL THEN
            SET p_result_message = '회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_board_exists FROM board WHERE id = p_board_id;
            IF v_board_exists = 0 THEN
                SET p_result_message = '존재하지 않는 게시글 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                IF v_member_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 회원 ID입니다.';
                    ROLLBACK;
                ELSE
                    SELECT COUNT(*) INTO v_count FROM like_storage WHERE board_id = p_board_id AND member_id = p_member_id;
                    IF v_count > 0 THEN
                        SET p_result_message = '이미 좋아요가 등록되어 있습니다.';
                        ROLLBACK;
                    ELSE
                        INSERT INTO like_storage (board_id, member_id, created_at, updated_at)
                        VALUES (p_board_id, p_member_id, NOW(), NOW());
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 좋아요 저장소 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM like_storage WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 좋아요 저장소가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_board_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_board_exists FROM board WHERE id = p_board_id;
                    IF v_board_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 게시글 ID입니다.';
                        ROLLBACK;
                    ELSE
                        IF p_member_id IS NOT NULL THEN
                            SELECT COUNT(*) INTO v_member_exists FROM member WHERE id = p_member_id;
                            IF v_member_exists = 0 THEN
                                SET p_result_message = '존재하지 않는 회원 ID입니다.';
                                ROLLBACK;
                            ELSE
                                UPDATE like_storage
                                SET board_id = IFNULL(p_board_id, board_id),
                                    member_id = IFNULL(p_member_id, member_id),
                                    updated_at = NOW()
                                WHERE id = p_id;
                                
                                SET p_affected_rows = ROW_COUNT();
                                SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                                COMMIT;
                            END IF;
                        ELSE
                            UPDATE like_storage
                            SET board_id = IFNULL(p_board_id, board_id),
                                updated_at = NOW()
                            WHERE id = p_id;
                            
                            SET p_affected_rows = ROW_COUNT();
                            SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                            COMMIT;
                        END IF;
                    END IF;
                ELSE
                    UPDATE like_storage
                    SET member_id = IFNULL(p_member_id, member_id),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM like_storage WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 좋아요 저장소가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM like_storage WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSEIF p_board_id IS NOT NULL AND p_member_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_count FROM like_storage WHERE board_id = p_board_id AND member_id = p_member_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 좋아요 저장소가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM like_storage WHERE board_id = p_board_id AND member_id = p_member_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('좋아요 저장소가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        ELSE
            SET p_result_message = '삭제를 위해서는 ID 또는 board_id와 member_id가 필요합니다.';
            ROLLBACK;
        END IF;
    END IF;
    
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_purchase_history_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_member_id BIGINT,
    IN p_purchase_type VARCHAR(20),
    IN p_coupon_id BIGINT,
    IN p_voucher_id BIGINT,
    IN p_price INT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_member_id IS NULL THEN
            SET p_result_message = '회원 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_purchase_type IS NULL OR TRIM(p_purchase_type) = '' THEN
            SET p_result_message = '구매 유형은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_price IS NULL OR p_price < 0 THEN
            SET p_result_message = '결제 금액은 0 이상이어야 합니다.';
            ROLLBACK;
        ELSEIF p_purchase_type = 'COUPON' AND p_coupon_id IS NULL THEN
            SET p_result_message = '쿠폰 구매 시 쿠폰 ID는 필수입니다.';
            ROLLBACK;
        ELSEIF p_purchase_type = 'VOUCHER' AND p_voucher_id IS NULL THEN
            SET p_result_message = '바우처 구매 시 바우처 ID는 필수입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM member WHERE id = p_member_id;
            IF v_count = 0 THEN
                SET p_result_message = '존재하지 않는 회원 ID입니다.';
                ROLLBACK;
            ELSE
                INSERT INTO purchase_history (member_id, purchase_type, coupon_id, voucher_id, price, created_at, updated_at)
                VALUES (p_member_id, p_purchase_type, p_coupon_id, p_voucher_id, p_price, NOW(), NOW());
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('결제 이력이 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
                COMMIT;
            END IF;
        END IF;
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 결제 이력 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM purchase_history WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 결제 이력이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM purchase_history WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('결제 이력이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

DELIMITER ;
