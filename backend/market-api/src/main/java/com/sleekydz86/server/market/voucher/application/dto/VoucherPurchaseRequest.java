package com.sleekydz86.server.market.voucher.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VoucherPurchaseRequest(
        @NotNull(message = "쿠폰 ID를 입력해주세요")
        Long couponId,

        @NotBlank(message = "바우처 설명을 입력해주세요")
        String description
) {
}
