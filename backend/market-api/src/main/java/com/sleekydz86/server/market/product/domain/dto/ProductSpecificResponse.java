package com.sleekydz86.server.market.product.domain.dto;

import com.sleekydz86.server.market.category.domain.CategoryName;
import com.sleekydz86.server.market.product.domain.vo.ProductStatus;

import java.time.LocalDateTime;

public record ProductSpecificResponse(
        Long id,
        String location,
        String title,
        String content,
        Integer price,
        ProductStatus productStatus,
        Integer visitedCount,
        Integer contactCount,
        Long categoryId,
        String categoryName,
        Long ownerId,
        String ownerNickname,
        Boolean isMyProduct,
        Integer likedCount,
        Boolean isLikedAlreadyByMe,
        LocalDateTime createDate
) {

    public ProductSpecificResponse(final Long id, final Location location, final String title, final String content, final Integer price, final ProductStatus productStatus, final Integer visitedCount, final Integer contactCount, final Long categoryId, final Long ownerId, final String ownerNickname, final Boolean isMyProduct, final Integer likedCount, final Boolean isLikedAlreadyByMe, final LocalDateTime createDate) {
        this(id, location.getContent(), title, content, price, productStatus, visitedCount, contactCount, categoryId, from(categoryId), ownerId, ownerNickname, isMyProduct, likedCount, isLikedAlreadyByMe, createDate);
    }

    public static String from(final Long categoryId) {
        return CategoryName.from(categoryId)
                .getName();
    }
}
