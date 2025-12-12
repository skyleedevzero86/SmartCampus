package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.market.product.domain.ProductLike;

public interface ProductLikePersistencePort {

    boolean existsProductLikeByProductIdAndMemberId(final Long productId, final Long memberId);

    void deleteProductLikeByProductIdAndMemberId(final Long productId, final Long memberId);

    ProductLike saveProductLike(final ProductLike productLike);
}


