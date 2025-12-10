package com.sleekydz86.chat.model.domain.port.out;

public interface MessagePublishPort {

    void publishMessage(Long chatRoomId, ChatMessageRequest messageRequest);
}
