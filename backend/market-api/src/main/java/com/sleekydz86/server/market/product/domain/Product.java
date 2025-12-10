package com.sleekydz86.server.market.product.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import com.sleekydz86.server.global.exception.exceptions.market.ProductAlreadySoldOutException;
import com.sleekydz86.server.global.exception.exceptions.market.ProductOwnerNotEqualsException;
import com.sleekydz86.server.market.product.application.ProductImageConverter;
import com.sleekydz86.server.market.product.application.dto.ProductUpdateResult;
import com.sleekydz86.server.market.product.domain.vo.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    private Long id;

    private Description description;

    private Price price;

    private StatisticCount statisticCount;

    private ProductStatus productStatus;

    private Long categoryId;

    private Long memberId;

    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();


    public static Product create(
            final String title,
            final String content,
            final Location location,
            final Integer price,
            final Long categoryId,
            final Long memberId,
            final List<MultipartFile> imageFiles,
            final ProductImageConverter imageConverter
    ) {
        validateProductCreation(title, content, price, categoryId, memberId);
        return Product.builder()
                .description(new Description(title, content, location))
                .price(new Price(price))
                .statisticCount(StatisticCount.createDefault())
                .categoryId(categoryId)
                .memberId(memberId)
                .productStatus(ProductStatus.WAITING)
                .productImages(imageConverter.convertImageFilesToImages(imageFiles))
                .build();
    }

    private static void validateProductCreation(
            String title,
            String content,
            Integer price,
            Long categoryId,
            Long memberId
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("상품 제목은 비어있을 수 없습니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("상품 내용은 비어있을 수 없습니다.");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("상품 가격은 양수여야 합니다.");
        }
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("카테고리 ID는 양수여야 합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("회원 ID는 양수여야 합니다.");
        }
    }

    public ProductUpdateResult updateDescription(final String title, final String content, final Location location, final Integer price, final Long categoryId, final Long memberId, final List<MultipartFile> imageFiles, final List<Long> deletedImageIds, final ProductImageConverter imageConverter) {
        validateOwner(memberId);
        this.description.update(title, content, location);
        this.price = new Price(price);
        this.categoryId = categoryId;

        List<ProductImage> addedImages = imageConverter.convertImageFilesToImages(imageFiles);
        List<ProductImage> deletedImages = imageConverter.convertImageIdsToImages(deletedImageIds, this.productImages);

        this.productImages.addAll(addedImages);
        this.productImages.removeAll(deletedImages);

        return new ProductUpdateResult(addedImages, deletedImages);
    }

    public void validateOwner(final Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new ProductOwnerNotEqualsException();
        }
    }

    public void view(final boolean canAddViewCount) {
        this.statisticCount.view(canAddViewCount);
    }


    public void sell() {
        if (this.productStatus.isCompleted()) {
            throw new ProductAlreadySoldOutException();
        }
        this.productStatus = ProductStatus.COMPLETED;
    }


    public boolean isAvailable() {
        return this.productStatus == ProductStatus.WAITING;
    }


    public boolean isSold() {
        return this.productStatus == ProductStatus.COMPLETED;
    }

    public void likes(final boolean isNeedToIncrease) {
        this.statisticCount.likes(isNeedToIncrease);
    }
}
