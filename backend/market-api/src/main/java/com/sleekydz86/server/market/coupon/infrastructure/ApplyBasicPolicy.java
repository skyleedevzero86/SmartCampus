package com.sleekydz86.server.market.coupon.infrastructure;

import com.sleekydz86.server.market.coupon.domain.ApplyPolicy;
import com.sleekydz86.server.market.coupon.domain.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplyBasicPolicy implements ApplyPolicy { //퍼센트 할인진행을 위한 기능


    @Override
    public int apply(
            final Integer price,
            final List<Coupon> percentageCoupons,
            final List<Coupon> discountCoupons
    ) {
        int afterPrice = price;

        for (Coupon discountCoupon : discountCoupons) {
            afterPrice = discountCoupon.discount(afterPrice);
        }

        for (Coupon percentageCoupon : percentageCoupons) {
            afterPrice = percentageCoupon.discount(afterPrice);
        }

        return afterPrice;
    }
}