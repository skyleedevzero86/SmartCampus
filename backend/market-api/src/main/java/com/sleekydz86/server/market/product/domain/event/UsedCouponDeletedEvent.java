package com.sleekydz86.server.market.product.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class UsedCouponDeletedEvent {

    private final Long buyerId;
    private final List<Long> usedCoupons;
}