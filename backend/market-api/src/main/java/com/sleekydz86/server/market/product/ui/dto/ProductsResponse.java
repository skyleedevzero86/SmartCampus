package com.sleekydz86.server.market.product.ui.dto;

import com.sleekydz86.server.market.product.domain.Product;

public record ProductResponse(
        Long productId,
        Long ownerId,
        String title,
        String content,
        Integer price
) {

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getMemberId(),
                product.getDescription().getTitle(),
                product.getDescription().getContent(),
                product.getPrice().getPrice()
        );
    }
}