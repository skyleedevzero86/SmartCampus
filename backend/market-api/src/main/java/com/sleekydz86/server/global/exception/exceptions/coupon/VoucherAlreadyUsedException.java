package com.sleekydz86.server.global.exception.exceptions.coupon;

public class VoucherAlreadyUsedException extends RuntimeException {

    public VoucherAlreadyUsedException() {
        super("이미 사용한 바우처입니다.");
    }
}
