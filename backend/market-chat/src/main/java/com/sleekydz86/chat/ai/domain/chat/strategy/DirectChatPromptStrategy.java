package com.sleekydz86.chat.ai.domain.chat.strategy;

import com.sleekydz86.chat.ai.enums.ChatMode;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class DirectChatPromptStrategy implements PromptStrategy {
    
    @Override
    public Prompt createPrompt(String question) {
        return new Prompt(question);
    }
    
    @Override
    public ChatMode getSupportedMode() {
        return ChatMode.DIRECT;
    }
}

