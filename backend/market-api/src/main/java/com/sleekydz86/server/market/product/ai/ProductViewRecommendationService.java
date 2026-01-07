package com.sleekydz86.server.market.product.ai;

import com.sleekydz86.server.market.product.application.ProductQueryPort;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductViewRecommendationService {

    private final ChatClient chatClient;
    private final ProductQueryPort productQueryPort;

    public List<ProductPagingSimpleResponse> recommendSimilarProducts(Long viewedProductId, Long memberId) {
        try {
            com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse viewedProduct = 
                    productQueryPort.findSpecificProductById(viewedProductId, memberId)
                    .orElse(null);

            if (viewedProduct == null) {
                return List.of();
            }

            List<ProductPagingSimpleResponse> categoryProducts = productQueryPort
                    .findAllProductsInCategoryWithPaging(memberId, null, viewedProduct.categoryId(), 20);

            List<ProductPagingSimpleResponse> similarProducts = categoryProducts.stream()
                    .filter(p -> !p.getId().equals(viewedProductId))
                    .limit(10)
                    .collect(Collectors.toList());

            if (similarProducts.isEmpty()) {
                return List.of();
            }

            String recommendationPrompt = buildRecommendationPrompt(viewedProduct, similarProducts);
            String aiAnalysis = chatClient.prompt(new Prompt(recommendationPrompt))
                    .call()
                    .content();

            log.info("AI 상품 추천 분석: {}", aiAnalysis);

            return similarProducts.stream()
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("유사 상품 추천 실패: productId={}", viewedProductId, e);
            return List.of();
        }
    }

    private String buildRecommendationPrompt(com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse viewedProduct, 
                                            List<ProductPagingSimpleResponse> candidates) {
        String viewedInfo = String.format("조회한 상품: %s (%d원, 카테고리: %d)", 
                viewedProduct.title(), viewedProduct.price(), viewedProduct.categoryId());
        
        String candidatesInfo = candidates.stream()
                .limit(10)
                .map(p -> String.format("- %s (%d원)", p.title(), p.price()))
                .collect(Collectors.joining("\n"));

        return String.format("""
                사용자가 다음 상품을 조회했습니다:
                %s
                
                아래 후보 상품들 중에서 조회한 상품과 가장 유사하고 가치 있는 상품 5개를 추천해주세요.
                추천 기준: 가격 대비 가치, 상태, 인기도, 거래 가능성
                
                【후보 상품】
                %s
                
                추천할 상품 ID를 쉼표로 구분하여 반환해주세요 (예: 1, 3, 5, 7, 9)
                """, viewedInfo, candidatesInfo);
    }
}

