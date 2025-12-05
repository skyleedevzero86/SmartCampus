package com.sleekydz86.server.market.board.application.dto;

import java.time.LocalDateTime;

public record BoardFoundResponse(
        Long id,
        String writerNickname,
        String title,
        String content,
        Long likeCount,
        Boolean isMyPost,
        Boolean isLikedAlreadyByMe,
        LocalDateTime createdDate
) {
}
