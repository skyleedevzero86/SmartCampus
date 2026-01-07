package com.sleekydz86.chat.ai.domain.chat.strategy;

import org.springframework.ai.chat.prompt.Prompt;

public interface PromptStrategy {
    
    Prompt createPrompt(String question);
    
    com.sleekydz86.chat.ai.enums.ChatMode getSupportedMode();
}

