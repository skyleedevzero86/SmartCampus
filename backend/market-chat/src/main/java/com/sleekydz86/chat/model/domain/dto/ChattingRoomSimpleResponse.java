package com.sleekydz86.chat.model.domain.dto;

import java.time.LocalDateTime;

public record ChattingRoomSimpleResponse(
        String productName,
        Long productId,
        Long chattingRoomId,
        Long sellerId,
        String sellerNickname,
        LocalDateTime lastChattingTime
) {
}
