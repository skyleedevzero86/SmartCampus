package com.sleekydz86.chat.model.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    private static final int MAX_LENGTH = 1000;
    private static final int MIN_LENGTH = 1;

    @Lob
    @Column(name = "message", nullable = false)
    private String value;

    private Message(String value) {
        validate(value);
        this.value = value;
    }

    public static Message of(String value) {
        return new Message(value);
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("메시지는 비어있을 수 없습니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("메시지는 %d자 이상 %d자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(value, message.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}


