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
public class ProductShoppingAssistantService {

    private final ChatClient chatClient;
    private final ProductQueryPort productQueryPort;
    private final com.sleekydz86.server.market.product.ai.domain.search.SearchService searchService;

    public String getShoppingAdvice(Long productId, Long memberId, String userQuestion) {
        try {
            com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse product = 
                    productQueryPort.findSpecificProductById(productId, memberId)
                    .orElse(null);

            if (product == null) {
                return "상품 정보를 찾을 수 없습니다.";
            }

            List<com.sleekydz86.server.market.product.ai.domain.search.SearchResult> searchResults = 
                    searchService.search(product.title() + " 중고거래 가격 비교 구매 팁");

            String productInfo = String.format("""
                    상품명: %s
                    가격: %d원
                    카테고리 ID: %d
                    """, product.title(), product.price(), product.categoryId());

            String searchContext = "관련 정보를 찾을 수 없습니다.";
            if (searchResults != null && !searchResults.isEmpty()) {
                searchContext = searchResults.stream()
                        .limit(5)
                        .map(r -> String.format("제목: %s\n내용: %s\n링크: %s", 
                                r.getTitle(), r.getContent(), r.getUrl()))
                        .collect(Collectors.joining("\n\n---\n\n"));
            }

            String prompt = String.format("""
                    당신은 중고거래 구매 조언 전문가입니다. 사용자의 질문에 대해 검색된 정보와 상품 정보를 바탕으로 도움이 되는 조언을 제공해주세요.
                    
                    【상품 정보】
                    %s
                    
                    【검색된 정보】
                    %s
                    
                    【사용자 질문】
                    %s
                    
                    【답변 규칙】
                    1. 검색된 정보를 활용하여 가격 비교, 구매 시 주의사항, 시장 동향 등을 종합적으로 분석
                    2. 중고거래 특성상 주의해야 할 점(상품 상태 확인, 거래 안전성 등)을 포함
                    3. 구체적이고 실용적인 조언 제공
                    4. 300자 이내로 간결하게 작성
                    
                    답변해주세요:
                    """, productInfo, searchContext, userQuestion);

            return chatClient.prompt(new Prompt(prompt))
                    .call()
                    .content();

        } catch (Exception e) {
            log.error("구매 조언 생성 실패: productId={}", productId, e);
            return "죄송합니다. 조언을 생성하는 중 오류가 발생했습니다.";
        }
    }
}

