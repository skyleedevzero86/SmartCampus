package com.sleekydz86.server.market.product.ui.dto;

import com.sleekydz86.server.market.product.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public record ProductsResponse(
        List<ProductSimpleResponse> products
) {

    public static ProductsResponse from(final List<Product> products) {
        return products.stream()
                .map(ProductSimpleResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), ProductsResponse::new));
    }
}
