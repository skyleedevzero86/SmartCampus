package com.sleekydz86.server.market.product.ui;

import com.sleekydz86.server.market.member.ui.auth.support.AuthMember;
import com.sleekydz86.server.market.product.ai.application.ProductAiAgentService;
import com.sleekydz86.server.market.product.ai.domain.agent.*;
import com.sleekydz86.server.market.product.application.ProductQueryPort;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/ai/agent/products")
@RestController
public class ProductAiAgentController {

    private final ProductAiAgentService productAiAgentService;
    private final ProductQueryPort productQueryPort;

    @GetMapping("/{productId}/price-analysis")
    public ResponseEntity<PriceAnalysisResult> analyzePrice(
            @PathVariable Long productId,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId
    ) {
        ProductSpecificResponse product = productQueryPort.findSpecificProductById(productId, null)
                .orElse(null);
        
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        Integer productPrice = price != null ? price : product.price();
        String productTitle = title != null ? title : product.title();
        Long productCategoryId = categoryId != null ? categoryId : product.categoryId();
        
        PriceAnalysisResult result = productAiAgentService.analyzePrice(
                productId, productPrice, productTitle, productCategoryId
        );
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/improve-description")
    public ResponseEntity<DescriptionImprovementResult> improveDescription(
            @RequestParam String currentDescription,
            @RequestParam String title,
            @RequestParam Integer price
    ) {
        DescriptionImprovementResult result = productAiAgentService.improveDescription(
                currentDescription, title, price
        );
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}/safety-check")
    public ResponseEntity<SafetyCheckResult> checkSafety(
            @PathVariable Long productId,
            @AuthMember(required = false) Long memberId
    ) {
        ProductSpecificResponse product = productQueryPort.findSpecificProductById(productId, memberId)
                .orElse(null);
        
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        SafetyCheckResult result = productAiAgentService.checkSafety(product);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recommendations/similar")
    public ResponseEntity<List<ProductPagingSimpleResponse>> recommendSimilarProducts(
            @AuthMember Long memberId,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ProductPagingSimpleResponse> likedProducts = 
                productQueryPort.findLikesProducts(memberId);
        
        List<ProductPagingSimpleResponse> recommendations = 
                productAiAgentService.recommendSimilarProducts(likedProducts, categoryId);
        
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/evaluate-condition")
    public ResponseEntity<ProductConditionResult> evaluateCondition(
            @RequestParam String description,
            @RequestParam Integer price,
            @RequestParam String title
    ) {
        ProductConditionResult result = productAiAgentService.evaluateCondition(
                description, price, title
        );
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/smart-search")
    public ResponseEntity<List<ProductPagingSimpleResponse>> smartSearch(
            @RequestParam String query,
            @AuthMember(required = false) Long memberId,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ProductPagingSimpleResponse> results = 
                productAiAgentService.smartSearch(query, memberId, categoryId);
        
        return ResponseEntity.ok(results);
    }
}

