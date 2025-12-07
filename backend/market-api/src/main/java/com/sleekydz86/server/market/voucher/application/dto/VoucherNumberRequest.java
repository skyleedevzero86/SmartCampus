package com.sleekydz86.server.market.voucher.application.dto;

import jakarta.validation.constraints.NotBlank;

public record VoucherNumberRequest(
        @NotBlank(message = "바우처 번호를 입력해주세요.")
        String voucherNumber
) {
}
