package com.sleekydz86.chat.model.infrastructure;

import com.sleekydz86.chat.model.application.dto.ChatMessageRequest;
import com.sleekydz86.chat.model.domain.port.out.MessagePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessagePublishAdapter implements MessagePublishPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publishMessage(Long chatRoomId, ChatMessageRequest messageRequest) {
        messagingTemplate.convertAndSend("/sub/chats/" + chatRoomId, messageRequest);
    }
}





