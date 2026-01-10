package com.sleekydz86.server.market.purchase.domain;

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
public class PurchaseHistory extends BaseEntity {

    private Long id;

    private Long memberId;

    private PurchaseType purchaseType;

    private Long couponId;

    private Long voucherId;

    private Integer price;

    public static PurchaseHistory createCouponPurchase(final Long memberId, final Long couponId, final Integer price) {
        return PurchaseHistory.builder()
                .memberId(memberId)
                .purchaseType(PurchaseType.COUPON)
                .couponId(couponId)
                .voucherId(null)
                .price(price)
                .build();
    }

    public static PurchaseHistory createVoucherPurchase(final Long memberId, final Long voucherId, final Integer price) {
        return PurchaseHistory.builder()
                .memberId(memberId)
                .purchaseType(PurchaseType.VOUCHER)
                .couponId(null)
                .voucherId(voucherId)
                .price(price)
                .build();
    }

    public enum PurchaseType {
        COUPON, VOUCHER
    }
}
