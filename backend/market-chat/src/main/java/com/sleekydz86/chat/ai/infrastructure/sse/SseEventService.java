package com.sleekydz86.chat.ai.infrastructure.sse;

import com.sleekydz86.chat.ai.enums.SSEMsgType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEventService {
    
    SseEmitter connect(String userId);
    
    void sendMessage(String userId, String message, SSEMsgType msgType);
    
    void closeConnection(String userId);
}

