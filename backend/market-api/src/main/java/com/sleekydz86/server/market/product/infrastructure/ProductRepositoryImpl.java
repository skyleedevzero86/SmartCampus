package com.sleekydz86.server.market.product.infrastructure;

import com.sleekydz86.server.market.product.domain.Product;
import com.sleekydz86.server.market.product.domain.ProductLike;
import com.sleekydz86.server.market.product.domain.dto.ProductImageResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;
import com.sleekydz86.server.market.product.mapper.ProductLikeMapper;
import com.sleekydz86.server.market.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductRepositoryAdapter implements ProductPersistencePort, ProductQueryPort, ProductLikePersistencePort {

    private final ProductMapper productMapper;
    private final ProductQueryRepository productQueryRepository;
    private final ProductLikeMapper productLikeMapper;

    @Override
    public Product save(final Product product) {
        productMapper.save(product);
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
        productMapper.deleteById(productId);
    }

    @Override
    public Optional<ProductSpecificResponse> findSpecificProductById(final Long productId, final Long memberId) {
        return productQueryRepository.findSpecificProductById(productId, memberId);
    }

    @Override
    public List<ProductPagingSimpleResponse> findAllProductsInCategoryWithPaging(
            final Long memberId,
            final Long productId,
            final Long categoryId,
            final int pageSize
    ) {
        return productQueryRepository.findAllWithPagingByCategoryId(memberId, productId, categoryId, pageSize);
    }

    @Override
    public List<ProductPagingSimpleResponse> findLikesProducts(final Long memberId) {
        return productQueryRepository.findLikesProducts(memberId);
    }

    @Override
    public List<ProductImageResponse> findImages(final Long productId) {
        return productQueryRepository.findImages(productId);
    }

    @Override
    public boolean existsProductLikeByProductIdAndMemberId(final Long productId, final Long memberId) {
        return productLikeMapper.existsByProductIdAndMemberId(productId, memberId);
    }

    @Override
    public void deleteProductLikeByProductIdAndMemberId(final Long productId, final Long memberId) {
        productLikeMapper.deleteByProductIdAndMemberId(productId, memberId);
    }

    @Override
    public ProductLike saveProductLike(final ProductLike productLike) {
        productLikeMapper.save(productLike);
        return productLike;
    }
}
