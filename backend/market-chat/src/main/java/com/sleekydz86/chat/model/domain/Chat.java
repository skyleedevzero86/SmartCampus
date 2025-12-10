package com.sleekydz86.chat.model.domain;

import com.sleekydz86.chat.model.domain.vo.ChatRoomId;
import com.sleekydz86.chat.model.domain.vo.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Chat extends BaseEntity {

    private Long id;

    private ChatRoomId chatRoomId;

    private Long senderId;

    private Message message;

    /**
     * 팩토리 메서드: 채팅 메시지 생성
     * DDD 원칙에 따라 도메인 객체 생성 로직을 캡슐화합니다.
     */
    public static Chat create(
            final Long chatRoomId,
            final Long senderId,
            final String message
    ) {
        validateSender(senderId);
        return Chat.builder()
                .chatRoomId(ChatRoomId.of(chatRoomId))
                .senderId(senderId)
                .message(Message.of(message))
                .build();
    }

    private static void validateSender(Long senderId) {
        if (senderId == null || senderId <= 0) {
            throw new IllegalArgumentException("발신자 ID는 양수여야 합니다.");
        }
    }

    // JPA를 위한 접근자 메서드
    public Long getChatRoomIdValue() {
        return chatRoomId.getValue();
    }

    public String getMessageValue() {
        return message.getValue();
    }
}

