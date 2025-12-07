package com.sleekydz86.server.market.coupon.ui.dto;

import java.util.List;

public record ApplyCouponResponse(
        int originPrice,
        int discountPrice,
        List<Long> usingCouponIds
) {
}
