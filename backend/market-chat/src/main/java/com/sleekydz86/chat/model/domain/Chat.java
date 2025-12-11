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

    public Long getChatRoomIdValue() {
        return chatRoomId.getValue();
    }

    public String getMessageValue() {
        return message.getValue();
    }
}

