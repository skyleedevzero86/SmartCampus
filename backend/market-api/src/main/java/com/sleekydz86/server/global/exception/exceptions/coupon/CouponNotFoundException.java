package com.sleekydz86.server.global.exception.exceptions.coupon;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다");
    }
}
