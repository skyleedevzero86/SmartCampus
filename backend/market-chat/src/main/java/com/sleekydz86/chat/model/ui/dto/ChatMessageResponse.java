package com.sleekydz86.chat.model.ui.dto;

import com.sleekydz86.chat.model.domain.Chat;

public record ChatMessageResponse(
        Long chattingRoomId,
        Long chatId,
        Long senderId,
        String message
) {

    public static ChatMessageResponse from(final Chat chat) {
        return new ChatMessageResponse(
                chat.getChatRoomIdValue(),
                chat.getId(),
                chat.getSenderId(),
                chat.getMessageValue()
        );
    }
}

