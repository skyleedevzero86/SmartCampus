package com.sleekydz86.server.market.comment.domain.dto;

import java.time.LocalDateTime;

public record CommentSimpleResponse(
        Long id,
        String content,
        Long writerId,
        Boolean isMine,
        String writerNickname,
        LocalDateTime createDate
) {
}
