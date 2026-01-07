package com.sleekydz86.chat.ai.infrastructure.sse;

import com.sleekydz86.chat.ai.enums.SSEMsgType;
import com.sleekydz86.chat.ai.utils.SSEServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseEventServiceImpl implements SseEventService {
    
    @Override
    public SseEmitter connect(String userId) {
        return SSEServer.connect(userId);
    }
    
    @Override
    public void sendMessage(String userId, String message, SSEMsgType msgType) {
        SSEServer.sendMsg(userId, message, msgType);
    }
    
    @Override
    public void closeConnection(String userId) {
        SSEServer.close(userId);
    }
}

