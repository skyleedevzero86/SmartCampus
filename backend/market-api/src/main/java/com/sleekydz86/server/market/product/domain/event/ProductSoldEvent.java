package com.sleekydz86.server.market.product.domain.event;

import com.sleekydz86.server.global.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductSoldEvent extends Event {

    private final Long buyerId;
    private final Long sellerId;
    private final Long productId;
    private final Integer productOriginalPrice;
    private final Integer productDiscountPrice;
    private final List<Long> usingCouponIds;
}

