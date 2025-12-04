package com.sleekydz86.server.market.product.domain.event;

import com.sleekydz86.server.global.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CouponExistValidatedEvent extends Event {

    private final Long memberId;
    private final List<Long> usingCouponIds;
}