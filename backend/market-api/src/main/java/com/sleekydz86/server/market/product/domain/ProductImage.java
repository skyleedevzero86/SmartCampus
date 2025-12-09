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
public class ProductImage extends BaseEntity {

    private Long id;

    private Long productId;

    private String originName;

    private String uniqueName;

    public boolean isSameImageId(final Long id) {
        return this.id.equals(id);
    }
}
