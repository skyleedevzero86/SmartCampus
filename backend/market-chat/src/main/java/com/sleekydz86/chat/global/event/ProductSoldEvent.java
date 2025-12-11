package com.sleekydz86.chat.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProductSoldEvent {

    private final Long buyerId;
    private final Long sellerId;
    private final Long productId;
    private final Integer productOriginalPrice;
    private final Integer productDiscountPrice;
    private final List<Long> usingCouponIds;
}