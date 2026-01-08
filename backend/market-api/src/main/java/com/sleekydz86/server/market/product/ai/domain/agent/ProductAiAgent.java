package com.sleekydz86.server.market.product.ai.domain.agent;

import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;

import java.util.List;

public interface ProductAiAgent {
    
    PriceAnalysisResult analyzePrice(Long productId, Integer price, String title, Long categoryId);
    
    DescriptionImprovementResult improveDescription(String currentDescription, String title, Integer price);
    
    SafetyCheckResult checkSafety(ProductSpecificResponse product);
    
    List<ProductPagingSimpleResponse> recommendSimilarProducts(
            List<ProductPagingSimpleResponse> likedProducts, 
            Long categoryId
    );
    
    ProductConditionResult evaluateCondition(String description, Integer price, String title);
    
    List<ProductPagingSimpleResponse> smartSearch(String query, Long memberId, Long categoryId);
}

