package com.sleekydz86.server.market.product.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductLike extends BaseEntity {

    private Long id;

    private Long memberId;

    private Long productId;

    public static ProductLike from(final Long memberId, final Long productId) {
        return ProductLike.builder()
                .memberId(memberId)
                .productId(productId)
                .build();
    }
}