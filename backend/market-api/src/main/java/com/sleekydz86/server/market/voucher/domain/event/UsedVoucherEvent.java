package com.sleekydz86.server.market.voucher.domain.event;

public record UsedVoucherEvent(
        Long couponId,
        Long memberId
) {
}
