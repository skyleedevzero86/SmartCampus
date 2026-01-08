package com.sleekydz86.server.market.product.ui;

import com.sleekydz86.server.market.member.ui.auth.support.AuthMember;
import com.sleekydz86.server.market.product.ai.ProductAiService;
import com.sleekydz86.server.market.product.ai.application.ProductAiAgentService;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/ai/products")
@RestController
public class ProductAiController {

    private final ProductAiService productAiService;
    private final ProductAiAgentService productAiAgentService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<ProductPagingSimpleResponse>> recommendProducts(
            @AuthMember Long memberId,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ProductPagingSimpleResponse> recommendations = 
                productAiService.recommendProducts(memberId, categoryId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/generate-description")
    public ResponseEntity<String> generateDescription(
            @RequestParam String title,
            @RequestParam String categoryName,
            @RequestParam Integer price
    ) {
        String description = productAiService.generateProductDescription(title, categoryName, price);
        return ResponseEntity.ok(description);
    }

    @GetMapping("/smart-search")
    public ResponseEntity<List<ProductPagingSimpleResponse>> smartSearch(
            @RequestParam String query,
            @AuthMember Long memberId,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ProductPagingSimpleResponse> results = 
                productAiAgentService.smartSearch(query, memberId, categoryId);
        return ResponseEntity.ok(results);
    }
}

