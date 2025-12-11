-- ============================================
-- SmartCampus Market Chat Database Schema
-- ============================================

-- 채팅방 테이블
CREATE TABLE IF NOT EXISTS chatting_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '채팅방 고유 식별자',
    product_id BIGINT NOT NULL COMMENT '상품 식별자',
    buyer_id BIGINT NOT NULL COMMENT '구매자 회원 식별자',
    seller_id BIGINT NOT NULL COMMENT '판매자 회원 식별자',
    chatting_status VARCHAR(20) NOT NULL DEFAULT 'PROCESS' COMMENT '채팅 상태 (PROCESS:진행중, DONE:종료)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_chatting_room_product_id (product_id),
    INDEX idx_chatting_room_buyer_id (buyer_id),
    INDEX idx_chatting_room_seller_id (seller_id),
    INDEX idx_chatting_room_status (chatting_status),
    INDEX idx_chatting_room_created_at (created_at),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT,
    FOREIGN KEY (buyer_id) REFERENCES member(id) ON DELETE RESTRICT,
    FOREIGN KEY (seller_id) REFERENCES member(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='채팅방 정보 테이블';

-- 채팅 메시지 테이블
CREATE TABLE IF NOT EXISTS chat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '채팅 메시지 고유 식별자',
    chat_room_id BIGINT NOT NULL COMMENT '채팅방 식별자',
    sender_id BIGINT NOT NULL COMMENT '발신자 회원 식별자',
    message TEXT NOT NULL COMMENT '메시지 내용',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_chat_room_id (chat_room_id),
    INDEX idx_chat_sender_id (sender_id),
    INDEX idx_chat_created_at (created_at),
    FOREIGN KEY (chat_room_id) REFERENCES chatting_room(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES member(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='채팅 메시지 테이블';

