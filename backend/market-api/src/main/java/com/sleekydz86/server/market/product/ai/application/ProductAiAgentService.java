package com.sleekydz86.server.market.product.ai.application;

import com.sleekydz86.server.market.product.ai.domain.agent.*;
import com.sleekydz86.server.market.product.application.ProductQueryPort;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.domain.dto.ProductSpecificResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductAiAgentService implements ProductAiAgent {

    private final ChatClient chatClient;
    private final ProductQueryPort productQueryPort;

    @Override
    public PriceAnalysisResult analyzePrice(Long productId, Integer price, String title, Long categoryId) {
        try {
            List<ProductPagingSimpleResponse> similarProducts = 
                    productQueryPort.findAllProductsInCategoryWithPaging(null, productId, categoryId, 10);
            
            String similarPrices = similarProducts.stream()
                    .map(p -> String.format("- %s: %d원", p.title(), p.price()))
                    .limit(5)
                    .collect(Collectors.joining("\n"));
            
            String marketPriceInfo = similarProducts.isEmpty() 
                    ? "비교할 상품이 없습니다." 
                    : String.format("시장 가격 정보:\n%s", similarPrices);
            
            double avgPrice = similarProducts.stream()
                    .mapToInt(ProductPagingSimpleResponse::price)
                    .average()
                    .orElse(price);
            
            String prompt = String.format("""
                    당신은 중고거래 가격 분석 전문가입니다. 상품 가격의 적정성을 분석해주세요.
                    
                    【분석 대상 상품】
                    - 제목: %s
                    - 가격: %d원
                    - 카테고리 ID: %d
                    
                    【시장 가격 정보】
                    %s
                    - 평균 가격: %.0f원
                    
                    【분석 요청】
                    1. 시장 가격과 비교하여 가격 수준을 평가하세요 (TOO_HIGH, HIGH, FAIR, LOW, TOO_LOW)
                    2. 가격이 적정한지 분석하고 근거를 제시하세요
                    3. 적정하지 않다면 제안 가격을 제시하세요
                    4. 200자 이내로 간결하게 작성하세요
                    
                    다음 형식으로 응답해주세요:
                    LEVEL: [가격 수준]
                    ANALYSIS: [분석 내용]
                    SUGGESTED_PRICE: [제안 가격, 없으면 null]
                    REASONING: [근거]
                    """, title, price, categoryId, marketPriceInfo, avgPrice);
            
            String response = chatClient.prompt(new Prompt(prompt))
                    .call()
                    .content();
            
            return parsePriceAnalysis(response, price, avgPrice);
            
        } catch (Exception e) {
            log.error("가격 분석 실패: productId={}", productId, e);
            return new PriceAnalysisResult(
                    "가격 분석 중 오류가 발생했습니다.",
                    PriceAnalysisResult.PriceLevel.FAIR,
                    price,
                    "분석 실패"
            );
        }
    }

    @Override
    public DescriptionImprovementResult improveDescription(String currentDescription, String title, Integer price) {
        try {
            String prompt = String.format("""
                    당신은 중고거래 상품 설명 개선 전문가입니다. 기존 상품 설명을 분석하고 개선해주세요.
                    
                    【상품 정보】
                    - 제목: %s
                    - 가격: %d원
                    - 현재 설명: %s
                    
                    【개선 요청】
                    1. 기존 설명의 장점과 단점을 분석하세요
                    2. 더 매력적이고 신뢰할 수 있는 설명으로 개선하세요
                    3. 구체적인 개선 사항 3가지를 제시하세요
                    4. 200자 이내로 간결하게 작성하세요
                    
                    다음 형식으로 응답해주세요:
                    IMPROVED: [개선된 설명]
                    IMPROVEMENTS: [개선사항1|개선사항2|개선사항3]
                    REASONING: [개선 근거]
                    """, title, price, currentDescription);
            
            String response = chatClient.prompt(new Prompt(prompt))
                    .call()
                    .content();
            
            return parseDescriptionImprovement(response, currentDescription);
            
        } catch (Exception e) {
            log.error("설명 개선 실패", e);
            return new DescriptionImprovementResult(
                    currentDescription,
                    currentDescription,
                    List.of("개선 분석 중 오류가 발생했습니다."),
                    "분석 실패"
            );
        }
    }

    @Override
    public SafetyCheckResult checkSafety(ProductSpecificResponse product) {
        try {
            String prompt = String.format("""
                    당신은 중고거래 안전성 검증 전문가입니다. 상품 정보를 분석하여 거래 안전성을 체크해주세요.
                    
                    【상품 정보】
                    - 제목: %s
                    - 설명: %s
                    - 가격: %d원
                    - 상태: %s
                    - 조회수: %d
                    - 연락 횟수: %d
                    - 좋아요: %d
                    
                    【체크 항목】
                    1. 가격이 비정상적으로 낮거나 높은지
                    2. 설명이 모호하거나 부족한지
                    3. 의심스러운 표현이나 패턴이 있는지
                    4. 거래 시 주의사항
                    
                    다음 형식으로 응답해주세요:
                    LEVEL: [SAFE/CAUTION/RISKY]
                    WARNINGS: [경고1|경고2]
                    RECOMMENDATIONS: [권장1|권장2]
                    SUMMARY: [요약]
                    """, 
                    product.title(),
                    product.content(),
                    product.price(),
                    product.productStatus(),
                    product.visitedCount(),
                    product.contactCount(),
                    product.likedCount());
            
            String response = chatClient.prompt(new Prompt(prompt))
                    .call()
                    .content();
            
            return parseSafetyCheck(response);
            
        } catch (Exception e) {
            log.error("안전성 체크 실패: productId={}", product.id(), e);
            return new SafetyCheckResult(
                    SafetyCheckResult.SafetyLevel.CAUTION,
                    List.of("안전성 체크 중 오류가 발생했습니다."),
                    List.of("직접 확인이 필요합니다."),
                    "체크 실패"
            );
        }
    }

    @Override
    public List<ProductPagingSimpleResponse> recommendSimilarProducts(
            List<ProductPagingSimpleResponse> likedProducts, 
            Long categoryId
    ) {
        if (likedProducts == null || likedProducts.isEmpty()) {
            return List.of();
        }
        
        try {
            String likedProductsInfo = likedProducts.stream()
                    .limit(5)
                    .map(p -> String.format("- %s (%d원)", p.title(), p.price()))
                    .collect(Collectors.joining("\n"));
            
            List<ProductPagingSimpleResponse> candidates = 
                    productQueryPort.findAllProductsInCategoryWithPaging(null, null, categoryId, 20);
            
            List<Long> likedProductIds = likedProducts.stream()
                    .map(ProductPagingSimpleResponse::id)
                    .toList();
            
            List<ProductPagingSimpleResponse> filtered = candidates.stream()
                    .filter(p -> !likedProductIds.contains(p.id()))
                    .limit(10)
                    .toList();
            
            if (filtered.isEmpty()) {
                return List.of();
            }
            
            return filtered.stream()
                    .limit(5)
                    .toList();
            
        } catch (Exception e) {
            log.error("유사 상품 추천 실패", e);
            return List.of();
        }
    }

    @Override
    public ProductConditionResult evaluateCondition(String description, Integer price, String title) {
        try {
            String prompt = String.format("""
                    당신은 중고 상품 상태 평가 전문가입니다. 상품 설명과 가격을 바탕으로 상태를 평가해주세요.
                    
                    【상품 정보】
                    - 제목: %s
                    - 설명: %s
                    - 가격: %d원
                    
                    【평가 요청】
                    1. 설명을 분석하여 상품 상태를 평가하세요 (NEW, LIKE_NEW, GOOD, FAIR, POOR)
                    2. 평가 근거를 제시하세요
                    3. 개선 제안을 2가지 제시하세요
                    
                    다음 형식으로 응답해주세요:
                    LEVEL: [상태 수준]
                    DESCRIPTION: [상태 설명]
                    REASONING: [평가 근거]
                    SUGGESTIONS: [제안1|제안2]
                    """, title, description, price);
            
            String response = chatClient.prompt(new Prompt(prompt))
                    .call()
                    .content();
            
            return parseConditionResult(response);
            
        } catch (Exception e) {
            log.error("상품 상태 평가 실패", e);
            return new ProductConditionResult(
                    ProductConditionResult.ConditionLevel.FAIR,
                    "평가 중 오류가 발생했습니다.",
                    "분석 실패",
                    List.of("직접 확인이 필요합니다.")
            );
        }
    }

    @Override
    public List<ProductPagingSimpleResponse> smartSearch(String query, Long memberId, Long categoryId) {
        try {
            String intentPrompt = String.format("""
                    사용자의 검색 쿼리를 분석하여 주요 키워드와 의도를 추출해주세요.
                    검색 쿼리: %s
                    
                    주요 키워드 3개를 쉼표로 구분하여 반환해주세요.
                    예시: 노트북, 게이밍, 레노버
                    """, query);
            
            String keywords = chatClient.prompt(new Prompt(intentPrompt))
                    .call()
                    .content();
            
            log.info("AI 추출 키워드: {}", keywords);
            
            return productQueryPort.findAllProductsInCategoryWithPaging(memberId, null, categoryId, 20);
            
        } catch (Exception e) {
            log.error("스마트 검색 실패: query={}", query, e);
            return List.of();
        }
    }

    private PriceAnalysisResult parsePriceAnalysis(String response, Integer originalPrice, double avgPrice) {
        PriceAnalysisResult.PriceLevel level = PriceAnalysisResult.PriceLevel.FAIR;
        String analysis = "";
        Integer suggestedPrice = null;
        String reasoning = "";
        
        try {
            for (String line : response.split("\n")) {
                if (line.startsWith("LEVEL:")) {
                    String levelStr = line.substring(6).trim();
                    level = switch (levelStr) {
                        case "TOO_HIGH" -> PriceAnalysisResult.PriceLevel.TOO_HIGH;
                        case "HIGH" -> PriceAnalysisResult.PriceLevel.HIGH;
                        case "LOW" -> PriceAnalysisResult.PriceLevel.LOW;
                        case "TOO_LOW" -> PriceAnalysisResult.PriceLevel.TOO_LOW;
                        default -> PriceAnalysisResult.PriceLevel.FAIR;
                    };
                } else if (line.startsWith("ANALYSIS:")) {
                    analysis = line.substring(9).trim();
                } else if (line.startsWith("SUGGESTED_PRICE:")) {
                    String priceStr = line.substring(16).trim();
                    if (!priceStr.equals("null")) {
                        suggestedPrice = Integer.parseInt(priceStr);
                    }
                } else if (line.startsWith("REASONING:")) {
                    reasoning = line.substring(10).trim();
                }
            }
        } catch (Exception e) {
            log.warn("가격 분석 응답 파싱 실패", e);
        }
        
        if (analysis.isEmpty()) {
            analysis = response;
        }
        
        return new PriceAnalysisResult(analysis, level, suggestedPrice, reasoning);
    }

    private DescriptionImprovementResult parseDescriptionImprovement(String response, String original) {
        String improved = original;
        List<String> improvements = new ArrayList<>();
        String reasoning = "";
        
        try {
            for (String line : response.split("\n")) {
                if (line.startsWith("IMPROVED:")) {
                    improved = line.substring(9).trim();
                } else if (line.startsWith("IMPROVEMENTS:")) {
                    String[] items = line.substring(13).trim().split("\\|");
                    improvements = List.of(items);
                } else if (line.startsWith("REASONING:")) {
                    reasoning = line.substring(10).trim();
                }
            }
        } catch (Exception e) {
            log.warn("설명 개선 응답 파싱 실패", e);
        }
        
        if (improvements.isEmpty()) {
            improvements = List.of("AI 분석 완료");
        }
        
        return new DescriptionImprovementResult(improved, original, improvements, reasoning);
    }

    private SafetyCheckResult parseSafetyCheck(String response) {
        SafetyCheckResult.SafetyLevel level = SafetyCheckResult.SafetyLevel.CAUTION;
        List<String> warnings = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String summary = "";
        
        try {
            for (String line : response.split("\n")) {
                if (line.startsWith("LEVEL:")) {
                    String levelStr = line.substring(6).trim();
                    level = switch (levelStr) {
                        case "SAFE" -> SafetyCheckResult.SafetyLevel.SAFE;
                        case "RISKY" -> SafetyCheckResult.SafetyLevel.RISKY;
                        default -> SafetyCheckResult.SafetyLevel.CAUTION;
                    };
                } else if (line.startsWith("WARNINGS:")) {
                    String[] items = line.substring(9).trim().split("\\|");
                    warnings = List.of(items);
                } else if (line.startsWith("RECOMMENDATIONS:")) {
                    String[] items = line.substring(16).trim().split("\\|");
                    recommendations = List.of(items);
                } else if (line.startsWith("SUMMARY:")) {
                    summary = line.substring(8).trim();
                }
            }
        } catch (Exception e) {
            log.warn("안전성 체크 응답 파싱 실패", e);
        }
        
        if (warnings.isEmpty()) {
            warnings = List.of("직접 확인 필요");
        }
        if (recommendations.isEmpty()) {
            recommendations = List.of("안전한 거래를 위해 신중히 확인하세요");
        }
        
        return new SafetyCheckResult(level, warnings, recommendations, summary);
    }

    private ProductConditionResult parseConditionResult(String response) {
        ProductConditionResult.ConditionLevel level = ProductConditionResult.ConditionLevel.FAIR;
        String description = "";
        String reasoning = "";
        List<String> suggestions = new ArrayList<>();
        
        try {
            for (String line : response.split("\n")) {
                if (line.startsWith("LEVEL:")) {
                    String levelStr = line.substring(6).trim();
                    level = switch (levelStr) {
                        case "NEW" -> ProductConditionResult.ConditionLevel.NEW;
                        case "LIKE_NEW" -> ProductConditionResult.ConditionLevel.LIKE_NEW;
                        case "GOOD" -> ProductConditionResult.ConditionLevel.GOOD;
                        case "POOR" -> ProductConditionResult.ConditionLevel.POOR;
                        default -> ProductConditionResult.ConditionLevel.FAIR;
                    };
                } else if (line.startsWith("DESCRIPTION:")) {
                    description = line.substring(12).trim();
                } else if (line.startsWith("REASONING:")) {
                    reasoning = line.substring(10).trim();
                } else if (line.startsWith("SUGGESTIONS:")) {
                    String[] items = line.substring(12).trim().split("\\|");
                    suggestions = List.of(items);
                }
            }
        } catch (Exception e) {
            log.warn("상품 상태 평가 응답 파싱 실패", e);
        }
        
        if (suggestions.isEmpty()) {
            suggestions = List.of("상품 상태를 직접 확인하세요");
        }
        
        return new ProductConditionResult(level, description, reasoning, suggestions);
    }
}

