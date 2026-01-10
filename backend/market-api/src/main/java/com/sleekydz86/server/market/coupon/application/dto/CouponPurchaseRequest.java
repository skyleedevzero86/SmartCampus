package com.sleekydz86.server.market.coupon.application.dto;

import jakarta.validation.constraints.NotNull;

public record CouponPurchaseRequest(
        @NotNull(message = "쿠폰 ID를 입력해주세요")
        Long couponId
) {
}
