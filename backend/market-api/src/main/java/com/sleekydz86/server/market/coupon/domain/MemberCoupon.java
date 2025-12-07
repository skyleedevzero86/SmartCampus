package com.sleekydz86.server.market.coupon.domain;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon extends BaseEntity {

    private Long id;

    private Long memberId;

    private Long couponId;

    public static MemberCoupon of(final Long memberId, final Long couponId) {
        return MemberCoupon.builder()
                .memberId(memberId)
                .couponId(couponId)
                .build();
    }
}