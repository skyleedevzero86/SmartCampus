package com.sleekydz86.chat.model.domain.port.out;

public interface ChatQueryPort {

    List<ChatHistoryResponse> findChattingHistoryByChatId(
            Long authId,
            Long chattingRoomId,
            Long chatId,
            Integer pageSize
    );
}
