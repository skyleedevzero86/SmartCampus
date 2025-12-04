package com.sleekydz86.server.market.product.ui.dto;


import com.sleekydz86.server.market.product.domain.Product;

public record ProductSimpleResponse(
        Long productId,
        String title,
        Integer price
) {

    public static ProductSimpleResponse from(final Product product) {
        return new ProductSimpleResponse(
                product.getId(),
                product.getDescription().getTitle(),
                product.getPrice().getPrice()
        );
    }
}
