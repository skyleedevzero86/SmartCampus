package com.sleekydz86.chat.ai.domain.chat.strategy;

import com.sleekydz86.chat.ai.enums.ChatMode;
import com.sleekydz86.chat.ai.domain.search.SearchService;
import com.sleekydz86.chat.ai.domain.search.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InternetSearchPromptStrategy implements PromptStrategy {
    
    private static final String INTERNET_SEARCH_PROMPT_TEMPLATE = """
            당신은 실시간 네트워크 검색 능력을 가진 지능형 어시스턴트입니다. 아래 제공된 최신 네트워크 검색 결과를 기반으로 사용자의 질문에 답변하세요.
            규칙:
            1. 모든 검색 결과를 종합적으로 분석하여 사용자에게 포괄적이고 정확하며 일관된 답변을 제공하세요.
            2. 답변에서 "검색 결과에 따르면..."을 직접 인용하지 말고 자연스럽게 언어를 구성하세요.
            3. 검색 결과가 충분한 정보를 제공하지 못했다면, 솔직하게 사용자에게 알려주세요: "현재 검색 결과로는 귀하의 질문에 대한 정확한 정보를 찾을 수 없습니다."
            4. 답변은 간결하고 명확하며 핵심을 짚어야 합니다.

            【네트워크 검색 결과】
            {context}
                        
            【사용자 질문】
            {question}
            """;
    
    private final SearchService searchService;
    
    @Override
    public Prompt createPrompt(String question) {
        List<SearchResult> searchResults = searchService.search(question);
        String context = "유효한 네트워크 검색 결과를 가져올 수 없습니다.";
        
        if (!CollectionUtils.isEmpty(searchResults)) {
            context = searchResults.stream()
                    .map(result -> String.format("【출처 제목】: %s\n【내용 요약】: %s\n【링크】: %s",
                            result.getTitle(),
                            result.getContent(),
                            result.getUrl()))
                    .collect(Collectors.joining("\n\n---\n\n"));
        }
        
        String promptContent = INTERNET_SEARCH_PROMPT_TEMPLATE
                .replace("{context}", context)
                .replace("{question}", question);
        
        return new Prompt(promptContent);
    }
    
    @Override
    public ChatMode getSupportedMode() {
        return ChatMode.INTERNET_SEARCH;
    }
}

