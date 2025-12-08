package com.sleekydz86.server.market.coupon.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import com.sleekydz86.server.global.exception.exceptions.coupon.CouponAmountRangeInvalidException;
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
public class Coupon extends BaseEntity {

    private static final int MINIMUM_PERCENTAGE_DISCOUNT_AMOUNT = 0;
    private static final int MAXIMUM_PERCENTAGE_DISCOUNT_AMOUNT = 100;

    private Long id;

    public Description description;

    public Policy policy;

    public static Coupon createCoupon(
            final String name,
            final String content,
            final boolean canUseAlone,
            final boolean isDiscountPercentage,
            final int amount
    ) {
        validateAmountRange(isDiscountPercentage, amount);
        return Coupon.builder()
                .description(new Description(name, content))
                .policy(new Policy(canUseAlone, isDiscountPercentage, amount))
                .build();
    }

    private static void validateAmountRange(final boolean isDiscountPercentage, final int amount) {
        if (isDiscountPercentage && (amount <= MINIMUM_PERCENTAGE_DISCOUNT_AMOUNT || amount > MAXIMUM_PERCENTAGE_DISCOUNT_AMOUNT)) {
            throw new CouponAmountRangeInvalidException();
        }
    }

    public boolean isUsingAloneCoupon() {
        return this.policy.isCanUseAlone();
    }

    public boolean isPercentageCoupon() {
        return this.policy.isDiscountPercentage();
    }

    public int discount(final Integer price) {
        return this.policy.discount(price);
    }
}
