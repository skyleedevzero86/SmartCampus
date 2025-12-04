package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.market.product.domain.dto.ProductImageResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryPort productQueryPort;


    public List<ProductPagingSimpleResponse> findAllProductsInCategory(
            final Long memberId,
            final Long productId,
            final Long categoryId,
            final int pageSize
    ) {
        validatePageSize(pageSize);
        return productQueryPort.findAllProductsInCategoryWithPaging(memberId, productId, categoryId, pageSize);
    }

    public ProductWithImageResponse findById(final Long productId, final Long memberId) {
        List<ProductImageResponse> images = productQueryPort.findImages(productId);
        ProductSpecificResponse product = productQueryPort.findSpecificProductById(productId, memberId)
                .orElseThrow(ProductNotFoundException::new);
        return new ProductWithImageResponse(product, images);
    }

    public List<ProductPagingSimpleResponse> findLikesProducts(final Long memberId) {
        return productQueryPort.findLikesProducts(memberId);
    }

    private void validatePageSize(int pageSize) {
        if (pageSize <= 0 || pageSize > 100) {
            throw new IllegalArgumentException("페이지 크기는 1 이상 100 이하여야 합니다.");
        }
    }
}
