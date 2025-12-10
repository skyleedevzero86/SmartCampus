package com.sleekydz86.chat.model.domain;

import lombok.Getter;

import java.util.List;


@Getter
public class ChattingRooms {

    private final List<ChattingRoom> chattingRooms;

    public ChattingRooms(final List<ChattingRoom> chattingRooms) {
        if (chattingRooms == null) {
            throw new IllegalArgumentException("채팅방 목록은 null일 수 없습니다.");
        }
        this.chattingRooms = List.copyOf(chattingRooms);
    }

    public void completeAll() {
        this.chattingRooms.forEach(ChattingRoom::complete);
    }

    public int size() {
        return chattingRooms.size();
    }

    public boolean isEmpty() {
        return chattingRooms.isEmpty();
    }
}

