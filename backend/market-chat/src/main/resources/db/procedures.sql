DELIMITER $$

-- ============================================
-- Chat 프로시저 (C: Insert, U: Update, D: Delete)
-- ============================================
CREATE PROCEDURE sp_chat_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_chat_room_id BIGINT,
    IN p_sender_id BIGINT,
    IN p_message TEXT,
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT,
    OUT p_generated_id BIGINT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_chat_room_exists INT DEFAULT 0;
    DECLARE v_sender_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
        SET p_generated_id = NULL;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
        SET p_generated_id = NULL;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    SET p_generated_id = NULL;
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_chat_room_id IS NULL THEN
            SET p_result_message = '채팅방 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_sender_id IS NULL THEN
            SET p_result_message = '발신자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_message IS NULL OR TRIM(p_message) = '' THEN
            SET p_result_message = '메시지는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_chat_room_exists FROM chatting_room WHERE id = p_chat_room_id;
            IF v_chat_room_exists = 0 THEN
                SET p_result_message = '존재하지 않는 채팅방 ID입니다.';
                ROLLBACK;
            ELSE
                SELECT COUNT(*) INTO v_sender_exists FROM member WHERE id = p_sender_id;
                IF v_sender_exists = 0 THEN
                    SET p_result_message = '존재하지 않는 발신자 ID입니다.';
                    ROLLBACK;
                ELSE
                    INSERT INTO chat (chat_room_id, sender_id, message, created_at, updated_at)
                    VALUES (p_chat_room_id, p_sender_id, p_message, NOW(), NOW());
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_generated_id = LAST_INSERT_ID();
                    SET p_result_message = CONCAT('채팅 메시지가 성공적으로 생성되었습니다. 생성된 ID: ', p_generated_id);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 채팅 메시지 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM chat WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 채팅 메시지가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                UPDATE chat
                SET message = IFNULL(NULLIF(TRIM(p_message), ''), message),
                    updated_at = NOW()
                WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('채팅 메시지가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 채팅 메시지 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM chat WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 채팅 메시지가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM chat WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('채팅 메시지가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

-- ============================================
-- ChattingRoom 프로시저 (C: Insert, U: Update, D: Delete)
-- ============================================
CREATE PROCEDURE sp_chatting_room_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_product_id BIGINT,
    IN p_buyer_id BIGINT,
    IN p_seller_id BIGINT,
    IN p_chatting_status VARCHAR(20),
    OUT p_result_message VARCHAR(255),
    OUT p_affected_rows INT,
    OUT p_generated_id BIGINT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_product_exists INT DEFAULT 0;
    DECLARE v_buyer_exists INT DEFAULT 0;
    DECLARE v_seller_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1
            @sqlstate = RETURNED_SQLSTATE,
            @errno = MYSQL_ERRNO,
            @text = MESSAGE_TEXT;
        SET p_result_message = CONCAT('오류 발생: ', @text);
        SET p_affected_rows = 0;
        SET p_generated_id = NULL;
    END;
    
    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
        ROLLBACK;
        SET p_result_message = '경고가 발생했습니다.';
        SET p_affected_rows = 0;
        SET p_generated_id = NULL;
    END;
    
    START TRANSACTION;
    
    SET p_affected_rows = 0;
    SET p_result_message = '';
    SET p_generated_id = NULL;
    
    IF p_operation NOT IN ('C', 'U', 'D') THEN
        SET p_result_message = '잘못된 작업 코드입니다. C(생성), U(수정), D(삭제)만 사용 가능합니다.';
        ROLLBACK;
    ELSEIF p_operation = 'C' THEN
        IF p_product_id IS NULL THEN
            SET p_result_message = '상품 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_buyer_id IS NULL THEN
            SET p_result_message = '구매자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_seller_id IS NULL THEN
            SET p_result_message = '판매자 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_buyer_id = p_seller_id THEN
            SET p_result_message = '구매자와 판매자는 동일할 수 없습니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
            IF v_product_exists = 0 THEN
                SET p_result_message = '존재하지 않는 상품 ID입니다.';
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
                        INSERT INTO chatting_room (product_id, buyer_id, seller_id, chatting_status, created_at, updated_at)
                        VALUES (p_product_id, p_buyer_id, p_seller_id, IFNULL(p_chatting_status, 'PROCESS'), NOW(), NOW());
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_generated_id = LAST_INSERT_ID();
                        SET p_result_message = CONCAT('채팅방이 성공적으로 생성되었습니다. 생성된 ID: ', p_generated_id);
                        COMMIT;
                    END IF;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 채팅방 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM chatting_room WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 채팅방이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                IF p_product_id IS NOT NULL THEN
                    SELECT COUNT(*) INTO v_product_exists FROM product WHERE id = p_product_id;
                    IF v_product_exists = 0 THEN
                        SET p_result_message = '존재하지 않는 상품 ID입니다.';
                        ROLLBACK;
                    ELSE
                        UPDATE chatting_room
                        SET product_id = IFNULL(p_product_id, product_id),
                            buyer_id = IFNULL(p_buyer_id, buyer_id),
                            seller_id = IFNULL(p_seller_id, seller_id),
                            chatting_status = IFNULL(p_chatting_status, chatting_status),
                            updated_at = NOW()
                        WHERE id = p_id;
                        
                        SET p_affected_rows = ROW_COUNT();
                        SET p_result_message = CONCAT('채팅방이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                        COMMIT;
                    END IF;
                ELSE
                    UPDATE chatting_room
                    SET buyer_id = IFNULL(p_buyer_id, buyer_id),
                        seller_id = IFNULL(p_seller_id, seller_id),
                        chatting_status = IFNULL(p_chatting_status, chatting_status),
                        updated_at = NOW()
                    WHERE id = p_id;
                    
                    SET p_affected_rows = ROW_COUNT();
                    SET p_result_message = CONCAT('채팅방이 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                    COMMIT;
                END IF;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 채팅방 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM chatting_room WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 채팅방이 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM chatting_room WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('채팅방이 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

DELIMITER ;

