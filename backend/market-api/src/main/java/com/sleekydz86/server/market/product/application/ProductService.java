package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.global.event.Events;
import com.sleekydz86.server.market.product.application.dto.UsingCouponRequest;
import com.sleekydz86.server.market.product.domain.Product;
import com.sleekydz86.server.market.product.domain.ProductLike;
import com.sleekydz86.server.market.product.domain.event.CouponExistValidatedEvent;
import com.sleekydz86.server.market.product.domain.event.UsedCouponDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductPersistencePort productPersistencePort;
    private final ProductLikePersistencePort productLikePersistencePort;
    private final ProductImageConverter productImageConverter;
    private final ProductImageUploadPort productImageUploadPort;

    public Long uploadProduct(
            final Long memberId,
            final Long categoryId,
            final ProductCreateRequest request
    ) {
        Product product = Product.create(
                request.title(),
                request.content(),
                request.location(),
                request.price(),
                categoryId,
                memberId,
                request.images(),
                productImageConverter
        );
        Product savedProduct = productPersistencePort.save(product);
        productImageUploadPort.upload(product.getProductImages(), request.images());
        return savedProduct.getId();
    }

    public Product addViewCount(final Long productId, final Boolean canAddViewCount) {
        Product product = findProductWithPessimisticLock(productId);
        product.view(canAddViewCount);
        return product;
    }


    public void update(
            final Long productId,
            final Long memberId,
            final ProductUpdateRequest request
    ) {
        Product product = findProduct(productId);
        ProductUpdateResult result = product.updateDescription(
                request.title(),
                request.content(),
                request.location(),
                request.price(),
                request.categoryId(),
                memberId,
                request.addedImages(),
                request.deletedImages(),
                productImageConverter
        );

        productImageUploadPort.upload(result.addedImages(), request.addedImages());
        productImageUploadPort.delete(result.deletedImages());
    }


    public void delete(final Long productId, final Long memberId) {
        Product product = findProduct(productId);
        product.validateOwner(memberId);
        productPersistencePort.deleteProductById(productId);
    }

    public void buyProducts(
            final Long productId,
            final Long buyerId,
            final UsingCouponRequest request
    ) {
        Events.raise(new CouponExistValidatedEvent(buyerId, request.usingCouponIds()));

        Product product = findProduct(productId);
        product.sell();

        Events.raise(new UsedCouponDeletedEvent(buyerId, request.usingCouponIds()));
        Events.raise(new ProductSoldEvent(
                buyerId,
                product.getMemberId(),
                productId,
                request.productOriginalPrice(),
                request.productDiscountPrice(),
                request.usingCouponIds()
        ));
    }


    public boolean likes(final Long productId, final Long memberId) {
        Product product = findProductWithPessimisticLock(productId);
        boolean shouldIncreaseLikeCount = shouldIncreaseLikeCount(productId, memberId);
        product.likes(shouldIncreaseLikeCount);
        return shouldIncreaseLikeCount;
    }

    private Product findProduct(final Long productId) {
        return productPersistencePort.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Product findProductWithPessimisticLock(final Long productId) {
        return productPersistencePort.findByIdWithPessimisticLock(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private boolean shouldIncreaseLikeCount(final Long productId, final Long memberId) {
        if (productLikePersistencePort.existsProductLikeByProductIdAndMemberId(productId, memberId)) {
            productLikePersistencePort.deleteProductLikeByProductIdAndMemberId(productId, memberId);
            return false;
        }

        productLikePersistencePort.saveProductLike(ProductLike.from(memberId, productId));
        return true;
    }
}
