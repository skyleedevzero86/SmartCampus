package com.sleekydz86.server.market.voucher.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voucher extends BaseEntity {

    private Long id;

    private Long couponId;

    private String description;

    private String voucherNumber;

    private Boolean isPublic;

    private Boolean isUsed;

    public static Voucher createPrivate(
            final Long couponId,
            final String description,
            final VoucherNumberGenerator generator
    ) {
        return Voucher.builder()
                .couponId(couponId)
                .description(description)
                .voucherNumber(generator.generate())
                .isPublic(false)
                .isUsed(false)
                .build();
    }

    public void use(final String voucherNumber) {
        validateNumberEquals(voucherNumber);
        validateAlreadyUsed();

        if (!this.isPublic) {
            this.isUsed = true;
        }
    }

    private void validateAlreadyUsed() {
        if (this.isUsed) {
            throw new VoucherAlreadyUsedException();
        }
    }

    private void validateNumberEquals(final String voucherNumber) {
        if (!this.voucherNumber.equals(voucherNumber)) {
            throw new VoucherNumbersNotEqualsException();
        }
    }
}
