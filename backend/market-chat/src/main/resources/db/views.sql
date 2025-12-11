-- ============================================
-- SmartCampus Market Chat Database Views
-- 복수건 조회를 위한 뷰
-- ============================================

-- ============================================
-- 채팅방 목록 뷰 (chatting_room + product + member)
-- ============================================
CREATE OR REPLACE VIEW v_chatting_room_list AS
SELECT 
    cr.id AS chatting_room_id,
    cr.product_id,
    cr.buyer_id,
    cr.seller_id,
    cr.chatting_status,
    cr.created_at,
    cr.updated_at,
    p.title AS product_name,
    seller.nickname AS seller_nickname,
    buyer.nickname AS buyer_nickname
FROM chatting_room cr
LEFT JOIN product p ON cr.product_id = p.id
LEFT JOIN member seller ON cr.seller_id = seller.id
LEFT JOIN member buyer ON cr.buyer_id = buyer.id;

-- ============================================
-- 채팅 히스토리 뷰 (chat + member)
-- ============================================
CREATE OR REPLACE VIEW v_chat_history AS
SELECT 
    c.id AS chatting_id,
    c.chat_room_id,
    c.sender_id,
    c.message,
    c.created_at AS send_time,
    c.updated_at,
    m.nickname AS sender_nickname
FROM chat c
LEFT JOIN member m ON c.sender_id = m.id;

