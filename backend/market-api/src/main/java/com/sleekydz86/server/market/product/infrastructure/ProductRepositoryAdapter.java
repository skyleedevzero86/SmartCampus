package com.sleekydz86.server.market.product.infrastructure;

import com.sleekydz86.server.market.product.application.ProductLikePersistencePort;
import com.sleekydz86.server.market.product.application.ProductPersistencePort;
import com.sleekydz86.server.market.product.application.ProductQueryPort;
import com.sleekydz86.server.market.product.domain.Product;
import com.sleekydz86.server.market.product.domain.ProductLike;
import com.sleekydz86.server.market.product.domain.dto.ProductImageResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;
import com.sleekydz86.server.market.product.infrastructure.mapper.ProductLikeMapper;
import com.sleekydz86.server.market.product.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductRepositoryAdapter implements ProductPersistencePort, ProductQueryPort, ProductLikePersistencePort {

    private final ProductMapper productMapper;
    private final ProductLikeMapper productLikeMapper;

    @Override
    public Product save(final Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("title", product.getDescription().getTitle());
        params.put("content", product.getDescription().getContent());
        params.put("location", product.getDescription().getLocation().getContent());
        params.put("price", product.getPrice().getPrice());
        params.put("viewCount", product.getStatisticCount().getVisitedCount());
        params.put("likeCount", product.getStatisticCount().getLikedCount());
        params.put("productStatus", product.getProductStatus().name());
        params.put("categoryId", product.getCategoryId());
        params.put("memberId", product.getMemberId());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        productMapper.executeProductCUD(params);
        return product;
    }

    @Override
    public Optional<Product> findById(final Long productId) {
        return productMapper.findById(productId);
    }

    @Override
    public Optional<Product> findByIdWithPessimisticLock(final Long productId) {
        return productMapper.findByIdWithPessimisticLock(productId);
    }

    @Override
    public void deleteProductById(final Long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", productId);
        params.put("title", null);
        params.put("content", null);
        params.put("location", null);
        params.put("price", null);
        params.put("viewCount", null);
        params.put("likeCount", null);
        params.put("productStatus", null);
        params.put("categoryId", null);
        params.put("memberId", null);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        productMapper.executeProductCUD(params);
    }

    @Override
    public Optional<ProductSpecificResponse> findSpecificProductById(final Long productId, final Long memberId) {
        throw new UnsupportedOperationException("findSpecificProductById needs to be implemented with MyBatis");
    }

    @Override
    public List<ProductPagingSimpleResponse> findAllProductsInCategoryWithPaging(
            final Long memberId,
            final Long productId,
            final Long categoryId,
            final int pageSize
    ) {
        throw new UnsupportedOperationException("findAllProductsInCategoryWithPaging needs to be implemented with MyBatis");
    }

    @Override
    public List<ProductPagingSimpleResponse> findLikesProducts(final Long memberId) {
        throw new UnsupportedOperationException("findLikesProducts needs to be implemented with MyBatis");
    }

    @Override
    public List<ProductImageResponse> findImages(final Long productId) {
        throw new UnsupportedOperationException("findImages needs to be implemented with MyBatis");
    }

    @Override
    public boolean existsProductLikeByProductIdAndMemberId(final Long productId, final Long memberId) {
        return productLikeMapper.existsByProductIdAndMemberId(productId, memberId);
    }

    @Override
    public void deleteProductLikeByProductIdAndMemberId(final Long productId, final Long memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", null);
        params.put("memberId", memberId);
        params.put("productId", productId);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        productLikeMapper.executeProductLikeCUD(params);
    }

    @Override
    public ProductLike saveProductLike(final ProductLike productLike) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("memberId", productLike.getMemberId());
        params.put("productId", productLike.getProductId());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        productLikeMapper.executeProductLikeCUD(params);
        return productLike;
    }
}

