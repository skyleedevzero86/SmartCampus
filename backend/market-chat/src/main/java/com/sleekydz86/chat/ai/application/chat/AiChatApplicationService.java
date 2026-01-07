package com.sleekydz86.chat.ai.application.chat;

import com.sleekydz86.chat.ai.bean.AiChatRequest;
import com.sleekydz86.chat.ai.domain.chat.strategy.PromptStrategy;
import com.sleekydz86.chat.ai.domain.chat.strategy.PromptStrategyFactory;
import com.sleekydz86.chat.ai.enums.ChatMode;
import com.sleekydz86.chat.ai.enums.SSEMsgType;
import com.sleekydz86.chat.ai.infrastructure.sse.SseEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatApplicationService {
    
    private final ChatClient chatClient;
    private final PromptStrategyFactory promptStrategyFactory;
    private final SseEventService sseEventService;
    
    public void streamChat(AiChatRequest request) {
        String userId = request.getUserId();
        String question = request.getMessage();
        ChatMode mode = request.getMode() != null ? request.getMode() : ChatMode.DIRECT;
        
        PromptStrategy strategy = promptStrategyFactory.getStrategy(mode);
        Prompt prompt = strategy.createPrompt(question);
        
        log.info("【사용자: {}】【{} 모드】로 질문 중입니다.", userId, mode);
        
        Flux<String> stream = chatClient.prompt(prompt).stream().content();
        stream.doOnError(throwable -> {
                    log.error("【사용자: {}】의 AI 스트림 처리 중 오류 발생: {}", userId, throwable.getMessage(), throwable);
                    sseEventService.sendMessage(userId, "죄송합니다. 서비스에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.", SSEMsgType.FINISH);
                    sseEventService.closeConnection(userId);
                })
                .subscribe(
                        content -> sseEventService.sendMessage(userId, content, SSEMsgType.ADD),
                        error -> log.error("【사용자: {}】의 스트림 구독 최종 실패: {}", userId, error.getMessage()),
                        () -> {
                            log.info("【사용자: {}】의 스트림이 성공적으로 종료되었습니다.", userId);
                            sseEventService.sendMessage(userId, "done", SSEMsgType.FINISH);
                            sseEventService.closeConnection(userId);
                        }
                );
    }
}

