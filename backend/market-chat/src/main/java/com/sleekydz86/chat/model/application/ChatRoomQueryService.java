package com.sleekydz86.chat.model.application;

import com.sleekydz86.chat.model.domain.dto.ChatHistoryResponse;
import com.sleekydz86.chat.model.domain.dto.ChattingRoomSimpleResponse;
import com.sleekydz86.chat.model.domain.port.out.ChatQueryPort;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomQueryService {

    private final ChattingRoomQueryPort chattingRoomQueryPort;
    private final ChatQueryPort chatQueryPort;

    public List<ChattingRoomSimpleResponse> findAllMyChats(final Long authId) {
        validateAuthId(authId);
        return chattingRoomQueryPort.findMyChattingRooms(authId);
    }


    public List<ChatHistoryResponse> findChattingHistoryByChatId(
            final Long authId,
            final Long chattingRoomId,
            final Long chatId,
            final Integer pageSize
    ) {
        validateAuthId(authId);
        validateChattingRoomId(chattingRoomId);
        validatePageSize(pageSize);
        return chatQueryPort.findChattingHistoryByChatId(authId, chattingRoomId, chatId, pageSize);
    }

    private void validateAuthId(Long authId) {
        if (authId == null || authId <= 0) {
            throw new IllegalArgumentException("인증된 사용자 ID는 양수여야 합니다.");
        }
    }

    private void validateChattingRoomId(Long chattingRoomId) {
        if (chattingRoomId == null || chattingRoomId <= 0) {
            throw new IllegalArgumentException("채팅방 ID는 양수여야 합니다.");
        }
    }

    private void validatePageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("페이지 크기는 양수여야 합니다.");
        }
        if (pageSize > 100) {
            throw new IllegalArgumentException("페이지 크기는 100을 초과할 수 없습니다.");
        }
    }
}

