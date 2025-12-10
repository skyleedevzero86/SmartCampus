package com.sleekydz86.chat.model.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomId {

    @Column(name = "chat_room_id", nullable = false)
    private Long value;

    private ChatRoomId(Long value) {
        validate(value);
        this.value = value;
    }

    public static ChatRoomId of(Long value) {
        return new ChatRoomId(value);
    }

    private void validate(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("채팅방 ID는 양수여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomId that = (ChatRoomId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

