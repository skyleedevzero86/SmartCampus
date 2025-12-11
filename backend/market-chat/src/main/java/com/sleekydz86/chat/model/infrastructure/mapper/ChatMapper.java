package com.sleekydz86.chat.model.infrastructure.mapper;

import com.sleekydz86.chat.model.domain.Chat;
import com.sleekydz86.chat.model.domain.dto.ChatHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    void save(Chat chat);

    List<ChatHistoryResponse> findChattingHistoryByChatId(
            @Param("authId") Long authId,
            @Param("chattingRoomId") Long chattingRoomId,
            @Param("chatId") Long chatId,
            @Param("pageSize") Integer pageSize
    );
}