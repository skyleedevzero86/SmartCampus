-- ============================================
-- SmartCampus Market Chat Dummy Data
-- 각 테이블당 1000건 생성
-- ============================================

-- 채팅방 데이터 (1000건) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_chatting_room_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_product_id BIGINT;
    DECLARE v_buyer_id BIGINT;
    DECLARE v_seller_id BIGINT;
    WHILE i <= 1000 DO
        SET v_product_id = 1 + FLOOR(RAND() * 1000);
        SET v_buyer_id = 1 + FLOOR(RAND() * 1000);
        SET v_seller_id = 1 + FLOOR(RAND() * 1000);
        
        IF v_buyer_id != v_seller_id THEN
            IF NOT EXISTS (SELECT 1 FROM chatting_room WHERE product_id = v_product_id AND buyer_id = v_buyer_id AND seller_id = v_seller_id) THEN
                INSERT INTO chatting_room (product_id, buyer_id, seller_id, chatting_status, created_at, updated_at)
                VALUES (
                    v_product_id,
                    v_buyer_id,
                    v_seller_id,
                    ELT(1 + FLOOR(RAND() * 2), 'PROCESS', 'DONE'),
                    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                    NOW()
                );
                SET i = i + 1;
            END IF;
        END IF;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_chatting_room_dummy();
DROP PROCEDURE sp_insert_chatting_room_dummy;

-- 채팅 메시지 데이터 (1000건, 채팅방별 분배) - 프로시저 사용
DELIMITER $$
CREATE PROCEDURE sp_insert_chat_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_chat_room_id BIGINT;
    DECLARE v_sender_id BIGINT;
    DECLARE message_count INT;
    DECLARE j INT;
    WHILE i <= 1000 DO
        SET v_chat_room_id = 1 + FLOOR(RAND() * 1000);
        SET message_count = 1 + FLOOR(RAND() * 10);
        SET j = 1;
        WHILE j <= message_count AND i <= 1000 DO
            SELECT buyer_id, seller_id INTO @buyer_id, @seller_id FROM chatting_room WHERE id = v_chat_room_id LIMIT 1;
            SET v_sender_id = IF(FLOOR(RAND() * 2) = 0, @buyer_id, @seller_id);
            
            INSERT INTO chat (chat_room_id, sender_id, message, created_at, updated_at)
            VALUES (
                v_chat_room_id,
                v_sender_id,
                CONCAT('채팅 메시지 내용입니다. ', i, '번째 메시지입니다.'),
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
                NOW()
            );
            SET i = i + 1;
            SET j = j + 1;
        END WHILE;
    END WHILE;
END$$
DELIMITER ;

CALL sp_insert_chat_dummy();
DROP PROCEDURE sp_insert_chat_dummy;

