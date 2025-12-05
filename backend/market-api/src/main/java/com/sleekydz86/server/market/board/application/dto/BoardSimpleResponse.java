package com.sleekydz86.server.market.board.application.dto;




import java.time.LocalDateTime;

public record BoardSimpleResponse(
        Long id,
        String writerNickname,
        String title,
        LocalDateTime createdDate,
        Long likeCount,
        Long commentCount,
        Boolean isLikedAlreadyByMe
) {
}
