-- ============================================
-- SmartCampus Market Database Schema
-- ============================================

-- 회원 테이블
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 식별자',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일 주소',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호 (암호화)',
    nickname VARCHAR(50) NOT NULL COMMENT '닉네임',
    member_role VARCHAR(20) NOT NULL DEFAULT 'MEMBER' COMMENT '회원 권한 (MEMBER, ADMIN)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_member_email (email),
    INDEX idx_member_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보 테이블';

-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 고유 식별자',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '카테고리명 (A000:전자제품, A001:서적, A002:의류, A003:화장품, A004:기타)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품 카테고리 테이블';

-- 상품 테이블
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 고유 식별자',
    title VARCHAR(255) NOT NULL COMMENT '상품명',
    content TEXT NOT NULL COMMENT '상품 설명',
    location VARCHAR(50) NOT NULL COMMENT '거래 장소 (BUILDING_THREE:동사무소, BUILDING_FIVE:경로당, BUILDING_LIBRARY:편의점, BUILDING_CENTER:보건소, NEAR_MJU:놀이공원)',
    price INT NOT NULL DEFAULT 0 COMMENT '상품 가격',
    view_count INT NOT NULL DEFAULT 0 COMMENT '조회 수',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    product_status VARCHAR(20) NOT NULL DEFAULT 'WAITING' COMMENT '상품 상태 (WAITING:대기중, RESERVED:예약중, COMPLETED:판매완료)',
    category_id BIGINT NOT NULL COMMENT '카테고리 식별자',
    member_id BIGINT NOT NULL COMMENT '판매자 회원 식별자',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_product_category_id (category_id),
    INDEX idx_product_member_id (member_id),
    INDEX idx_product_status (product_status),
    INDEX idx_product_created_at (created_at),
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT,
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품 정보 테이블';

-- 상품 이미지 테이블
CREATE TABLE IF NOT EXISTS product_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 이미지 고유 식별자',
    product_id BIGINT NOT NULL COMMENT '상품 식별자',
    origin_name VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    unique_name VARCHAR(255) NOT NULL COMMENT '고유 파일명 (UUID)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_product_image_product_id (product_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품 이미지 테이블';

-- 상품 좋아요 테이블
CREATE TABLE IF NOT EXISTS product_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 좋아요 고유 식별자',
    member_id BIGINT NOT NULL COMMENT '회원 식별자',
    product_id BIGINT NOT NULL COMMENT '상품 식별자',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    UNIQUE KEY uk_product_like_member_product (member_id, product_id),
    INDEX idx_product_like_member_id (member_id),
    INDEX idx_product_like_product_id (product_id),
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품 좋아요 테이블';

-- 게시판 테이블
CREATE TABLE IF NOT EXISTS board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 고유 식별자',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    writer_id BIGINT NOT NULL COMMENT '작성자 회원 식별자',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_board_writer_id (writer_id),
    INDEX idx_board_created_at (created_at),
    FOREIGN KEY (writer_id) REFERENCES member(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 테이블';

-- 게시판 이미지 테이블
CREATE TABLE IF NOT EXISTS image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 고유 식별자',
    board_id BIGINT NOT NULL COMMENT '게시글 식별자',
    origin_name VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    unique_name VARCHAR(255) NOT NULL COMMENT '고유 파일명 (UUID)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_image_finding (board_id),
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 이미지 테이블';

-- 게시판 좋아요 저장소 테이블
CREATE TABLE IF NOT EXISTS like_storage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '좋아요 저장소 고유 식별자',
    board_id BIGINT NOT NULL COMMENT '게시글 식별자',
    member_id BIGINT NOT NULL COMMENT '회원 식별자',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    UNIQUE KEY uk_like_storage_board_member (board_id, member_id),
    INDEX idx_like_storage_board_id (board_id),
    INDEX idx_like_storage_member_id (member_id),
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 좋아요 저장소 테이블';

-- 댓글 테이블
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 식별자',
    content TEXT NOT NULL COMMENT '댓글 내용',
    writer_id BIGINT NOT NULL COMMENT '작성자 회원 식별자',
    board_id BIGINT NOT NULL COMMENT '게시글 식별자',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_comment_writer_id (writer_id),
    INDEX idx_comment_board_id (board_id),
    INDEX idx_comment_created_at (created_at),
    FOREIGN KEY (writer_id) REFERENCES member(id) ON DELETE RESTRICT,
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='댓글 테이블';

-- 쿠폰 테이블
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 고유 식별자',
    name VARCHAR(255) NOT NULL COMMENT '쿠폰명',
    content TEXT NOT NULL COMMENT '쿠폰 설명',
    can_use_alone BOOLEAN NOT NULL DEFAULT FALSE COMMENT '단독 사용 가능 여부',
    is_discount_percentage BOOLEAN NOT NULL DEFAULT FALSE COMMENT '할인율 여부 (TRUE:할인율, FALSE:할인금액)',
    amount INT NOT NULL DEFAULT 0 COMMENT '할인 금액 또는 할인율',
    price INT NOT NULL DEFAULT 0 COMMENT '쿠폰 구매 가격',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_coupon_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='쿠폰 정보 테이블';

-- 회원 쿠폰 테이블
CREATE TABLE IF NOT EXISTS member_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 쿠폰 고유 식별자',
    member_id BIGINT NOT NULL COMMENT '회원 식별자',
    coupon_id BIGINT NOT NULL COMMENT '쿠폰 식별자',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_member_coupon_member_id (member_id),
    INDEX idx_member_coupon_coupon_id (coupon_id),
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 보유 쿠폰 테이블';

-- 바우처 테이블
CREATE TABLE IF NOT EXISTS voucher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '바우처 고유 식별자',
    coupon_id BIGINT NOT NULL COMMENT '쿠폰 식별자',
    description VARCHAR(255) COMMENT '바우처 설명',
    voucher_number VARCHAR(255) NOT NULL UNIQUE COMMENT '바우처 번호',
    is_public BOOLEAN NOT NULL DEFAULT FALSE COMMENT '공개 여부',
    is_used BOOLEAN NOT NULL DEFAULT FALSE COMMENT '사용 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_voucher_coupon_id (coupon_id),
    INDEX idx_voucher_number (voucher_number),
    INDEX idx_voucher_is_used (is_used),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='바우처 테이블';

-- 거래 내역 테이블
CREATE TABLE IF NOT EXISTS trade_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 내역 고유 식별자',
    buyer_id BIGINT NOT NULL COMMENT '구매자 회원 식별자',
    seller_id BIGINT NOT NULL COMMENT '판매자 회원 식별자',
    product_id BIGINT NOT NULL COMMENT '상품 식별자',
    product_origin_price INT NOT NULL DEFAULT 0 COMMENT '상품 원가',
    product_discount_price INT NOT NULL DEFAULT 0 COMMENT '할인 적용 후 가격',
    using_coupon_ids VARCHAR(500) COMMENT '사용한 쿠폰 ID 목록 (콤마 구분)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_trade_history_buyer_id (buyer_id),
    INDEX idx_trade_history_seller_id (seller_id),
    INDEX idx_trade_history_product_id (product_id),
    INDEX idx_trade_history_created_at (created_at),
    FOREIGN KEY (buyer_id) REFERENCES member(id) ON DELETE RESTRICT,
    FOREIGN KEY (seller_id) REFERENCES member(id) ON DELETE RESTRICT,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='거래 내역 테이블';

-- 결제 이력 테이블
CREATE TABLE IF NOT EXISTS purchase_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 이력 고유 식별자',
    member_id BIGINT NOT NULL COMMENT '구매자 회원 식별자',
    purchase_type VARCHAR(20) NOT NULL COMMENT '구매 유형 (COUPON:쿠폰, VOUCHER:바우처)',
    coupon_id BIGINT COMMENT '쿠폰 식별자 (쿠폰 구매 시)',
    voucher_id BIGINT COMMENT '바우처 식별자 (바우처 구매 시)',
    price INT NOT NULL DEFAULT 0 COMMENT '결제 금액',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_purchase_history_member_id (member_id),
    INDEX idx_purchase_history_coupon_id (coupon_id),
    INDEX idx_purchase_history_voucher_id (voucher_id),
    INDEX idx_purchase_history_created_at (created_at),
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE RESTRICT,
    FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE RESTRICT,
    FOREIGN KEY (voucher_id) REFERENCES voucher(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='결제 이력 테이블';
