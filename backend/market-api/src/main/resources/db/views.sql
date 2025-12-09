-- ============================================
-- SmartCampus Market Database Views
-- JOIN 쿼리를 뷰로 변환
-- ============================================

-- ============================================
-- 게시판 상세 뷰 (board + member + comment count)
-- ============================================
CREATE OR REPLACE VIEW v_board_detail AS
SELECT 
    b.id,
    b.title,
    b.content,
    b.writer_id,
    b.like_count,
    b.created_at,
    b.updated_at,
    m.nickname AS writer_nickname,
    (SELECT COUNT(*) FROM comment c WHERE c.board_id = b.id) AS comment_count
FROM board b
LEFT JOIN member m ON b.writer_id = m.id;

-- ============================================
-- 거래 내역 상세 뷰 (trade_history + buyer + seller + product)
-- ============================================
CREATE OR REPLACE VIEW v_trade_history_detail AS
SELECT 
    th.id AS trade_history_id,
    th.buyer_id,
    th.seller_id,
    th.product_id,
    th.product_origin_price,
    th.product_discount_price,
    th.using_coupon_ids,
    th.created_at,
    th.updated_at,
    buyer.nickname AS buyer_name,
    seller.nickname AS seller_name,
    p.title AS product_title
FROM trade_history th
LEFT JOIN member buyer ON th.buyer_id = buyer.id
LEFT JOIN member seller ON th.seller_id = seller.id
LEFT JOIN product p ON th.product_id = p.id;

-- ============================================
-- 바우처 상세 뷰 (voucher + coupon)
-- ============================================
CREATE OR REPLACE VIEW v_voucher_detail AS
SELECT 
    v.id AS voucher_id,
    v.coupon_id,
    v.description AS voucher_description,
    v.voucher_number,
    v.is_public AS is_public_voucher,
    v.is_used AS is_used_voucher,
    v.created_at AS create_date,
    v.updated_at,
    c.name AS coupon_name,
    c.can_use_alone,
    c.is_discount_percentage,
    c.amount
FROM voucher v
LEFT JOIN coupon c ON v.coupon_id = c.id;

