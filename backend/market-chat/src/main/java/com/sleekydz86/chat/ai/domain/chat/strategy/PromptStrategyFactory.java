package com.sleekydz86.chat.ai.domain.chat.strategy;

import com.sleekydz86.chat.ai.enums.ChatMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PromptStrategyFactory {

    private final Map<ChatMode, PromptStrategy> strategyMap;

    public PromptStrategyFactory(List<PromptStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        PromptStrategy::getSupportedMode,
                        Function.identity()));
    }

    public PromptStrategy getStrategy(ChatMode mode) {
        PromptStrategy strategy = strategyMap.get(mode);
        if (strategy == null) {
            strategy = strategyMap.get(ChatMode.DIRECT);
        }
        return strategy;
    }
}

