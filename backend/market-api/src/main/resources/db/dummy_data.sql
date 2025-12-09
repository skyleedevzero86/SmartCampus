-- ============================================
-- SmartCampus Market Dummy Data
-- 각 테이블당 1000건 생성
-- ============================================

-- 카테고리 데이터 (5건)
INSERT INTO category (name, created_at, updated_at) VALUES
('A000', NOW(), NOW()),
('A001', NOW(), NOW()),
('A002', NOW(), NOW()),
('A003', NOW(), NOW()),
('A004', NOW(), NOW());

-- 회원 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_member_dummy()
BEGIN
    DECLARE i INT DEFAULT 6;
    WHILE i <= 1000 DO
        INSERT INTO member (email, password, nickname, member_role, created_at, updated_at)
        VALUES (
            CONCAT('user', i, '@example.com'),
            '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
            CONCAT('사용자', i),
            IF(i % 100 = 0, 'ADMIN', 'MEMBER'),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_member_dummy();
DROP PROCEDURE sp_insert_member_dummy;

-- 상품 데이터 (1000건, 카테고리별 분배) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_product_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO product (title, content, location, price, view_count, like_count, product_status, category_id, member_id, created_at, updated_at)
        VALUES (
            CONCAT('상품제목', i),
            CONCAT('상품 설명 내용입니다. ', i, '번째 상품의 상세 설명입니다.'),
            ELT(1 + FLOOR(RAND() * 5), 'BUILDING_THREE', 'BUILDING_FIVE', 'BUILDING_LIBRARY', 'BUILDING_CENTER', 'NEAR_MJU'),
            FLOOR(1000 + RAND() * 99000),
            FLOOR(RAND() * 1000),
            FLOOR(RAND() * 500),
            ELT(1 + FLOOR(RAND() * 3), 'WAITING', 'RESERVED', 'COMPLETED'),
            1 + FLOOR(RAND() * 5),
            1 + FLOOR(RAND() * 1000),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_product_dummy();
DROP PROCEDURE sp_insert_product_dummy;

-- 게시판 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_board_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO board (title, content, writer_id, like_count, created_at, updated_at)
        VALUES (
            CONCAT('게시글 제목', i),
            CONCAT('게시글 내용입니다. ', i, '번째 게시글의 상세 내용입니다.'),
            1 + FLOOR(RAND() * 1000),
            FLOOR(RAND() * 500),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_board_dummy();
DROP PROCEDURE sp_insert_board_dummy;

-- 댓글 데이터 (1000건, 게시판별 분배) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_comment_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO comment (content, writer_id, board_id, created_at, updated_at)
        VALUES (
            CONCAT('댓글 내용입니다. ', i, '번째 댓글입니다.'),
            1 + FLOOR(RAND() * 1000),
            1 + FLOOR(RAND() * 1000),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_comment_dummy();
DROP PROCEDURE sp_insert_comment_dummy;

-- 쿠폰 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_coupon_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO coupon (name, content, can_use_alone, is_discount_percentage, amount, created_at, updated_at)
        VALUES (
            CONCAT('쿠폰명', i),
            CONCAT('쿠폰 설명입니다. ', i, '번째 쿠폰의 상세 설명입니다.'),
            IF(FLOOR(RAND() * 2) = 0, FALSE, TRUE),
            IF(FLOOR(RAND() * 2) = 0, FALSE, TRUE),
            IF(FLOOR(RAND() * 2) = 0, FLOOR(1000 + RAND() * 9000), FLOOR(1 + RAND() * 50)),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_coupon_dummy();
DROP PROCEDURE sp_insert_coupon_dummy;

-- 회원 쿠폰 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_member_coupon_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_member_id BIGINT;
    DECLARE v_coupon_id BIGINT;
    WHILE i <= 1000 DO
        SET v_member_id = 1 + FLOOR(RAND() * 1000);
        SET v_coupon_id = 1 + FLOOR(RAND() * 1000);
        
        IF NOT EXISTS (SELECT 1 FROM member_coupon WHERE member_id = v_member_id AND coupon_id = v_coupon_id) THEN
            INSERT INTO member_coupon (member_id, coupon_id, created_at, updated_at)
            VALUES (
                v_member_id,
                v_coupon_id,
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET i = i + 1;
        END IF;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_member_coupon_dummy();
DROP PROCEDURE sp_insert_member_coupon_dummy;

-- 상품 좋아요 데이터 (1000건, 상품별 분배) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_product_like_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_member_id BIGINT;
    DECLARE v_product_id BIGINT;
    WHILE i <= 1000 DO
        SET v_member_id = 1 + FLOOR(RAND() * 1000);
        SET v_product_id = 1 + FLOOR(RAND() * 1000);
        
        IF NOT EXISTS (SELECT 1 FROM product_like WHERE member_id = v_member_id AND product_id = v_product_id) THEN
            INSERT INTO product_like (member_id, product_id, created_at, updated_at)
            VALUES (
                v_member_id,
                v_product_id,
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET i = i + 1;
        END IF;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_product_like_dummy();
DROP PROCEDURE sp_insert_product_like_dummy;

-- 게시판 좋아요 저장소 데이터 (1000건, 게시판별 분배) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_like_storage_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_board_id BIGINT;
    DECLARE v_member_id BIGINT;
    WHILE i <= 1000 DO
        SET v_board_id = 1 + FLOOR(RAND() * 1000);
        SET v_member_id = 1 + FLOOR(RAND() * 1000);
        
        IF NOT EXISTS (SELECT 1 FROM like_storage WHERE board_id = v_board_id AND member_id = v_member_id) THEN
            INSERT INTO like_storage (board_id, member_id, created_at, updated_at)
            VALUES (
                v_board_id,
                v_member_id,
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET i = i + 1;
        END IF;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_like_storage_dummy();
DROP PROCEDURE sp_insert_like_storage_dummy;

-- 거래 내역 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_trade_history_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO trade_history (buyer_id, seller_id, product_id, product_origin_price, product_discount_price, using_coupon_ids, created_at, updated_at)
        VALUES (
            1 + FLOOR(RAND() * 1000),
            1 + FLOOR(RAND() * 1000),
            1 + FLOOR(RAND() * 1000),
            FLOOR(10000 + RAND() * 90000),
            FLOOR(5000 + RAND() * 85000),
            IF(FLOOR(RAND() * 2) = 0, NULL, CONCAT(1 + FLOOR(RAND() * 1000), ',', 1 + FLOOR(RAND() * 1000))),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_trade_history_dummy();
DROP PROCEDURE sp_insert_trade_history_dummy;

-- 바우처 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_voucher_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000 DO
        INSERT INTO voucher (coupon_id, description, voucher_number, is_public, is_used, created_at, updated_at)
        VALUES (
            1 + FLOOR(RAND() * 1000),
            CONCAT('바우처 설명', i),
            CONCAT('VOUCHER-', LPAD(i, 10, '0')),
            IF(FLOOR(RAND() * 2) = 0, FALSE, TRUE),
            IF(FLOOR(RAND() * 2) = 0, FALSE, TRUE),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_voucher_dummy();
DROP PROCEDURE sp_insert_voucher_dummy;

-- 상품 이미지 데이터 (상품당 1~3개) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_product_image_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE j INT;
    DECLARE image_count INT;
    WHILE i <= 1000 DO
        SET image_count = 1 + FLOOR(RAND() * 3);
        SET j = 1;
        WHILE j <= image_count DO
            INSERT INTO product_image (product_id, origin_name, unique_name, created_at, updated_at)
            VALUES (
                i,
                CONCAT('product_image_', i, '_', j, '.jpg'),
                CONCAT(REPLACE(UUID(), '-', ''), '.jpg'),
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET j = j + 1;
        END WHILE;
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_product_image_dummy();
DROP PROCEDURE sp_insert_product_image_dummy;

-- 게시판 이미지 데이터 (게시판당 1~3개) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_board_image_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE j INT;
    DECLARE image_count INT;
    WHILE i <= 1000 DO
        SET image_count = 1 + FLOOR(RAND() * 3);
        SET j = 1;
        WHILE j <= image_count DO
            INSERT INTO image (board_id, origin_name, unique_name, created_at, updated_at)
            VALUES (
                i,
                CONCAT('board_image_', i, '_', j, '.jpg'),
                CONCAT(REPLACE(UUID(), '-', ''), '.jpg'),
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET j = j + 1;
        END WHILE;
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_board_image_dummy();
DROP PROCEDURE sp_insert_board_image_dummy;
