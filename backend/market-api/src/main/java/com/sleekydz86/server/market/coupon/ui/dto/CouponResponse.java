package com.sleekydz86.server.market.coupon.ui.dto;


import com.sleekydz86.server.market.coupon.domain.Coupon;

public record CouponResponse(
        Long couponId,
        String name,
        String content,
        boolean canUseAlone,
        boolean isDiscountPercentage,
        int amount
) {

    public static CouponResponse from(final Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.description.getName(),
                coupon.description.getContent(),
                coupon.policy.isCanUseAlone(),
                coupon.policy.isDiscountPercentage(),
                coupon.policy.getAmount()
        );
    }
}
