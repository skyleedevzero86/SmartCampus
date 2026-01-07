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
public class ProductAiService {

    private final ChatClient chatClient;
    private final ProductQueryPort productQueryPort;

    public List<ProductPagingSimpleResponse> recommendProducts(Long memberId, Long categoryId) {
        List<ProductPagingSimpleResponse> likedProducts = productQueryPort.findLikesProducts(memberId);
        
        String recommendationPrompt = buildRecommendationPrompt(likedProducts, categoryId);
        String aiResponse = chatClient.prompt(new Prompt(recommendationPrompt))
                .call()
                .content();
        
        log.info("AI 추천 응답: {}", aiResponse);
        
        return findSimilarProducts(likedProducts, categoryId);
    }

    public String generateProductDescription(String title, String categoryName, Integer price) {
        String prompt = String.format("""
                당신은 중고거래 플랫폼의 상품 설명 작성 전문가입니다.
                아래 정보를 바탕으로 매력적이고 정직한 상품 설명을 작성해주세요.
                
                【상품 정보】
                - 제목: %s
                - 카테고리: %s
                - 가격: %d원
                
                【작성 규칙】
                1. 상품의 상태를 정직하게 설명하세요 (새상품, 거의 새것, 사용감 있음 등)
                2. 구매 시기, 사용 기간, 하자 유무를 명확히 기재하세요
                3. 판매 이유를 간단히 언급하세요
                4. 거래 가능 지역과 방법을 안내하세요
                5. 200자 이내로 간결하게 작성하세요
                6. 이모지나 과도한 강조는 피하세요
                
                상품 설명을 작성해주세요:
                """, title, categoryName, price);
        
        return chatClient.prompt(new Prompt(prompt))
                .call()
                .content();
    }

    public List<ProductPagingSimpleResponse> smartSearch(String query, Long memberId, Long categoryId) {
        String intentPrompt = String.format("""
                사용자의 검색 쿼리를 분석하여 주요 키워드를 추출해주세요.
                검색 쿼리: %s
                
                주요 키워드 3개를 쉼표로 구분하여 반환해주세요.
                예시: 노트북, 게이밍, 레노버
                """, query);
        
        String keywords = chatClient.prompt(new Prompt(intentPrompt))
                .call()
                .content();
        
        log.info("AI 추출 키워드: {}", keywords);
        
        return productQueryPort.findAllProductsInCategoryWithPaging(memberId, null, categoryId, 20);
    }

    private String buildRecommendationPrompt(List<ProductPagingSimpleResponse> likedProducts, Long categoryId) {
        String productsInfo = likedProducts.stream()
                .limit(5)
                .map(p -> String.format("- %s (%d원)", p.title(), p.price()))
                .collect(Collectors.joining("\n"));
        
        return String.format("""
                사용자가 좋아요한 상품 목록을 분석하여 추천 상품을 제안해주세요.
                
                【좋아요한 상품】
                %s
                
                이 사용자의 취향을 분석하여 추천할 상품의 특징을 설명해주세요.
                """, productsInfo);
    }

    private List<ProductPagingSimpleResponse> findSimilarProducts(
            List<ProductPagingSimpleResponse> likedProducts, Long categoryId) {
        if (likedProducts.isEmpty()) {
            return List.of();
        }
        
        return List.of();
    }
}

