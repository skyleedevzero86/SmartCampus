package com.sleekydz86.server.market.board.infrastructure;

import com.sleekydz86.server.market.board.domain.LikeStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeStorageJpaRepository extends JpaRepository<LikeStorage, Long> {

    LikeStorage save(final LikeStorage likeStorage);

    boolean existsByBoardIdAndMemberId(final Long boardId, final Long memberId);

    void deleteByBoardIdAndMemberId(final Long boardId, final Long memberId);
}
