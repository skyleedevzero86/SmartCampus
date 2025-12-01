package com.sleekydz86.server.member.domain.member;

import com.sleekydz86.server.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeHistory extends BaseEntity {

    private static final String JOINING_DELIMITER = ",";
    private static final String EMPTY_VALUE = "";

    private Long id;

    private Long buyerId;

    private Long sellerId;

    private Long productId;

    private int productOriginPrice;

    private int productDiscountPrice;

    private String usingCouponIds;

    public TradeHistory(final Long buyerId, final Long sellerId, final Long productId, final int productOriginPrice, final int productDiscountPrice, final List<Long> usingCouponIds) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.productOriginPrice = productOriginPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.usingCouponIds = getUsingCouponIds(usingCouponIds);
    }

    private String getUsingCouponIds(final List<Long> usingCouponIds) {
        if (usingCouponIds.isEmpty()) {
            return EMPTY_VALUE;
        }

        return usingCouponIds.stream()
                .map(String::valueOf)
                .collect(joining(JOINING_DELIMITER));
    }
}
