package com.sleekydz86.server.market.board.application.dto;

public record LikeResultResponse(
        Long boardId,
        boolean likeStatus
) {
}
