package com.sleekydz86.chat.ai.domain.chat.strategy;

import com.sleekydz86.chat.ai.enums.ChatMode;
import com.sleekydz86.chat.ai.domain.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KnowledgeBasePromptStrategy implements PromptStrategy {
    
    private static final String RAG_PROMPT_TEMPLATE = """
            아래 제공된 컨텍스트 지식베이스 내용을 기반으로 사용자의 질문에 답변하세요.
            규칙:
            1. 답변 시 컨텍스트 정보를 최대한 활용하되, 답변에서 "컨텍스트에 따르면" 또는 "지식베이스에 따르면" 등의 표현을 직접 언급하지 마세요.
            2. 컨텍스트에 질문에 답변하기에 충분한 정보가 없다면, 명확히 알려주세요: "현재 지식으로는 이 질문에 답변할 수 없습니다."
            3. 답변은 직접적이고 명확하며 관련성 있어야 합니다.

            【컨텍스트】
            {context}
                        
            【질문】
            {question}
            """;
    
    private final DocumentRepository documentRepository;
    
    @Override
    public Prompt createPrompt(String question) {
        List<Document> relatedDocs = documentRepository.findSimilarDocuments(question);
        String context = "관련 지식베이스 정보를 찾을 수 없습니다.";
        
        if (!CollectionUtils.isEmpty(relatedDocs)) {
            context = relatedDocs.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n---\n"));
        }
        
        String promptContent = RAG_PROMPT_TEMPLATE
                .replace("{context}", context)
                .replace("{question}", question);
        
        return new Prompt(promptContent);
    }
    
    @Override
    public ChatMode getSupportedMode() {
        return ChatMode.KNOWLEDGE_BASE;
    }
}

