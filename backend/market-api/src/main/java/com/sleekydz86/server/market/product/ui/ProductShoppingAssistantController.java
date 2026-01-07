package com.sleekydz86.server.market.product.ui;

import com.sleekydz86.server.market.member.ui.auth.support.AuthMember;
import com.sleekydz86.server.market.product.ai.ProductShoppingAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductShoppingAssistantController {

    private final ProductShoppingAssistantService shoppingAssistantService;

    @GetMapping("/{productId}/shopping-advice")
    public ResponseEntity<String> getShoppingAdvice(
            @PathVariable Long productId,
            @AuthMember Long memberId,
            @RequestParam(required = false, defaultValue = "이 상품 구매 시 주의사항과 가격 비교 정보를 알려주세요") String question
    ) {
        String advice = shoppingAssistantService.getShoppingAdvice(productId, memberId, question);
        return ResponseEntity.ok(advice);
    }
}

