package com.sleekydz86.server.market.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberCoupons {

    private final List<MemberCoupon> memberCoupons;

    public static MemberCoupons of(final Long memberId, final List<Long> couponIds) {
        List<MemberCoupon> memberCoupons = couponIds.stream()
                .map(couponId -> MemberCoupon.of(memberId, couponId))
                .toList();

        return new MemberCoupons(memberCoupons);
    }
}
