package com.sleekydz86.server.global.exception.exceptions.coupon;

public class VoucherNotFoundException extends RuntimeException {

    public VoucherNotFoundException() {
        super("바우처를 찾을 수 없습니다.");
    }
}
