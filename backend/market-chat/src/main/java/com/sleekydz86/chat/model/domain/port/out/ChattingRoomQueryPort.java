package com.sleekydz86.chat.model.domain.port.out;

public interface ChattingRoomQueryPort {

    List<ChattingRoomSimpleResponse> findMyChattingRooms(Long authId);
}
