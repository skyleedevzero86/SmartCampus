package com.sleekydz86.server.market.board.domain;

public interface LikeStorageRepository {

    LikeStorage save(final LikeStorage likeStorage);

    boolean existsByBoardIdAndMemberId(final Long boardId, final Long memberId);

    void deleteByBoardIdAndMemberId(final Long boardId, final Long memberId);
}
