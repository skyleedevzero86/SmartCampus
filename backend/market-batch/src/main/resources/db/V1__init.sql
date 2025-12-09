-- ============================================
-- SmartCampus Market Batch Database Schema
-- Mail Storage Table Definition
-- ============================================

-- 메일 저장소 테이블
-- 회원 가입 시 인증 메일 발송 대기/실패/완료 상태를 관리하는 테이블
CREATE TABLE IF NOT EXISTS mail_storage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '메일 저장소 고유 식별자',
    member_id BIGINT NULL COMMENT '회원 식별자 (회원 가입 시 발급된 회원 ID)',
    email VARCHAR(255) NOT NULL COMMENT '수신자 이메일 주소',
    nickname VARCHAR(255) NOT NULL COMMENT '수신자 닉네임',
    mail_status ENUM('WAIT', 'FAIL', 'DONE') NOT NULL DEFAULT 'WAIT' COMMENT '메일 발송 상태 (WAIT:대기중, FAIL:실패, DONE:완료)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성 일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정 일시',
    
    -- 인덱스 정의
    INDEX idx_mail_storage_member_id (member_id) COMMENT '회원 ID 조회 성능 향상',
    INDEX idx_mail_storage_email (email) COMMENT '이메일 조회 성능 향상',
    INDEX idx_mail_storage_status (mail_status) COMMENT '상태별 조회 성능 향상 (배치 작업용)',
    INDEX idx_mail_storage_created_at (created_at) COMMENT '생성일시 기반 정렬/조회 성능 향상',
    INDEX idx_mail_storage_status_created (mail_status, created_at) COMMENT '상태별 최신순 조회 성능 향상'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메일 발송 대기/실패/완료 상태 관리 테이블';

-- ============================================
-- Dummy Data Insert
-- 테스트 및 개발 환경용 샘플 데이터 생성
-- ============================================

-- 더미 데이터 삽입 프로시저
DELIMITER $$

CREATE PROCEDURE sp_insert_mail_storage_dummy()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE v_status VARCHAR(10);
    DECLARE v_status_ratio INT;
    
    -- 1000건의 더미 데이터 생성
    WHILE i <= 1000 DO
        -- 상태 분배: WAIT 60%, DONE 35%, FAIL 5%
        SET v_status_ratio = FLOOR(RAND() * 100);
        IF v_status_ratio < 60 THEN
            SET v_status = 'WAIT';
        ELSEIF v_status_ratio < 95 THEN
            SET v_status = 'DONE';
        ELSE
            SET v_status = 'FAIL';
        END IF;
        
        INSERT INTO mail_storage (
            member_id,
            email,
            nickname,
            mail_status,
            created_at,
            updated_at
        ) VALUES (
            -- member_id는 1~1000 사이 랜덤 (실제 member 테이블과 연동 시 참고)
            IF(i % 10 = 0, NULL, 1 + FLOOR(RAND() * 1000)),
            CONCAT('user', i, '@example.com'),
            CONCAT('테스트사용자', i),
            v_status,
            -- 생성일시는 최근 180일 내 랜덤
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 180) DAY),
            NOW()
        );
        
        SET i = i + 1;
    END WHILE;
    
    SELECT CONCAT('메일 저장소 더미 데이터 ', i - 1, '건이 생성되었습니다.') AS result;
END$$

DELIMITER ;

-- 더미 데이터 생성 실행
CALL sp_insert_mail_storage_dummy();

-- 프로시저 정리
DROP PROCEDURE IF EXISTS sp_insert_mail_storage_dummy;

-- 생성된 데이터 확인 쿼리 (주석 해제하여 사용)
-- SELECT 
--     mail_status,
--     COUNT(*) AS count,
--     ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM mail_storage), 2) AS percentage
-- FROM mail_storage
-- GROUP BY mail_status
-- ORDER BY mail_status;
