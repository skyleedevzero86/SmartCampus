package com.sleekydz86.server.market.product.application.dto;


import com.sleekydz86.server.market.product.domain.dto.ProductImageResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;

import java.util.List;

public record ProductWithImageResponse(
        ProductSpecificResponse product,
        List<ProductImageResponse> images
) {
}
