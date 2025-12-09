DELIMITER $$

CREATE PROCEDURE sp_mail_storage_cud(
    IN p_operation CHAR(1),
    IN p_id BIGINT,
    IN p_member_id BIGINT,
    IN p_email VARCHAR(255),
    IN p_nickname VARCHAR(255),
    IN p_mail_status VARCHAR(10),
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
        ELSEIF p_email IS NULL OR TRIM(p_email) = '' THEN
            SET p_result_message = '이메일은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSEIF p_nickname IS NULL OR TRIM(p_nickname) = '' THEN
            SET p_result_message = '닉네임은 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            INSERT INTO mail_storage (member_id, email, nickname, mail_status, created_at, updated_at)
            VALUES (
                p_member_id,
                p_email,
                p_nickname,
                IFNULL(p_mail_status, 'WAIT'),
                NOW(),
                NOW()
            );
            
            SET p_affected_rows = ROW_COUNT();
            SET p_result_message = CONCAT('메일 저장소가 성공적으로 생성되었습니다. 생성된 ID: ', LAST_INSERT_ID());
            COMMIT;
        END IF;
        
    ELSEIF p_operation = 'U' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '수정할 메일 저장소 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM mail_storage WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '수정할 메일 저장소가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                UPDATE mail_storage
                SET member_id = IFNULL(p_member_id, member_id),
                    email = IFNULL(NULLIF(TRIM(p_email), ''), email),
                    nickname = IFNULL(NULLIF(TRIM(p_nickname), ''), nickname),
                    mail_status = IFNULL(p_mail_status, mail_status),
                    updated_at = NOW()
                WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('메일 저장소가 성공적으로 수정되었습니다. 수정된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
        
    ELSEIF p_operation = 'D' THEN
        IF p_id IS NULL THEN
            SET p_result_message = '삭제할 메일 저장소 ID는 필수 입력 항목입니다.';
            ROLLBACK;
        ELSE
            SELECT COUNT(*) INTO v_count FROM mail_storage WHERE id = p_id FOR UPDATE;
            
            IF v_count = 0 THEN
                SET p_result_message = '삭제할 메일 저장소가 존재하지 않습니다.';
                ROLLBACK;
            ELSE
                DELETE FROM mail_storage WHERE id = p_id;
                
                SET p_affected_rows = ROW_COUNT();
                SET p_result_message = CONCAT('메일 저장소가 성공적으로 삭제되었습니다. 삭제된 행 수: ', p_affected_rows);
                COMMIT;
            END IF;
        END IF;
    END IF;
    
END$$

CREATE OR REPLACE VIEW v_mail_storage_by_status AS
SELECT 
    id,
    member_id,
    email,
    nickname,
    mail_status,
    created_at,
    updated_at
FROM mail_storage;

DELIMITER ;

