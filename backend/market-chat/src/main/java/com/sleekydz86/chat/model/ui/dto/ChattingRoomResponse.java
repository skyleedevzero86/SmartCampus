package com.sleekydz86.chat.model.ui.dto;

import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.vo.ChattingStatus;

public record ChattingRoomResponse(
        Long chatRoomId,
        Long productId,
        Long buyerId,
        Long sellerId,
        ChattingStatus chattingStatus
) {

    public static ChattingRoomResponse from(final ChattingRoom chattingRoom) {
        return new ChattingRoomResponse(chattingRoom.getId(), chattingRoom.getProductId(), chattingRoom.getBuyerId(), chattingRoom.getSellerId(), chattingRoom.getChattingStatus());
    }
}

