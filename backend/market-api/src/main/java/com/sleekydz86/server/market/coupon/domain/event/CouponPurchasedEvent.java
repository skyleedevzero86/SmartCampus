package com.sleekydz86.server.market.coupon.domain.event;

public record CouponPurchasedEvent(
        Long memberId,
        Long couponId,
        Integer price
) {
}
