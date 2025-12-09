package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.market.product.domain.dto.ProductImageResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;

import java.util.List;
import java.util.Optional;

public interface ProductQueryPort {

    Optional<ProductSpecificResponse> findSpecificProductById(final Long productId, final Long memberId);

    List<ProductPagingSimpleResponse> findAllProductsInCategoryWithPaging(
            final Long memberId,
            final Long productId,
            final Long categoryId,
            final int pageSize
    );

    List<ProductPagingSimpleResponse> findLikesProducts(final Long memberId);

    List<ProductImageResponse> findImages(final Long productId);
}

