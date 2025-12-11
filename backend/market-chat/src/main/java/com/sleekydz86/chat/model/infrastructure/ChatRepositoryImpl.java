package com.sleekydz86.chat.model.infrastructure;

import com.sleekydz86.chat.model.domain.Chat;
import com.sleekydz86.chat.model.domain.port.out.ChatPersistencePort;
import com.sleekydz86.chat.model.infrastructure.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ChatRepositoryImpl implements ChatPersistencePort {

    private final ChatMapper chatMapper;

    @Override
    public Chat save(final Chat chat) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("chatRoomId", chat.getChatRoomIdValue());
        params.put("senderId", chat.getSenderId());
        params.put("message", chat.getMessageValue());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        params.put("generatedId", null);
        
        chatMapper.executeChatCUD(params);
        
        Long generatedId = (Long) params.get("generatedId");
        if (generatedId != null) {
            return Chat.builder()
                    .id(generatedId)
                    .chatRoomId(chat.getChatRoomId())
                    .senderId(chat.getSenderId())
                    .message(chat.getMessage())
                    .build();
        }
        
        return chat;
    }
}
