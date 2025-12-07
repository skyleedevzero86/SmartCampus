package com.sleekydz86.server.market.coupon.application.dto;

import java.util.List;

public record CouponDeletedRequest(
        List<Long> deletedCouponIds
) {
}
