package com.sleekydz86.server.member.member.dto;

public record TradeHistoryResponse(
        Long tradeHistoryId,
        String buyerName,
        String sellerName,
        Long productId,
        String productTitle,
        int productOriginPrice,
        int productDiscountPrice,
        String usingCouponIds
) {
}
