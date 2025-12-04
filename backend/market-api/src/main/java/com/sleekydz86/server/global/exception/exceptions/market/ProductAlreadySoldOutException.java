package com.sleekydz86.server.global.exception.exceptions.market;

public class ProductAlreadySoldOutException extends RuntimeException {

    public ProductAlreadySoldOutException() {
        super("이미 품절된 상품은 구매할 수 없습니다");
    }
}
