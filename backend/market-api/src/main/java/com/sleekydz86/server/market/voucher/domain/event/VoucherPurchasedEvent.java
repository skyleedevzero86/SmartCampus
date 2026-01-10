package com.sleekydz86.server.market.voucher.domain.event;

public record VoucherPurchasedEvent(
        Long memberId,
        Long voucherId,
        Integer price
) {
}
