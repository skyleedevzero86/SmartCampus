package com.sleekydz86.server.market.board.application;

import com.sleekydz86.server.global.event.Events;
import com.sleekydz86.server.market.board.domain.LikeStorageRepository;
import com.sleekydz86.server.market.board.domain.event.LikePushedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeStorageRepository likeStorageRepository;

    @Transactional
    public boolean patchLike(final Long boardId, final Long memberId) {
        boolean isNeedToIncrease = doesNeedToIncreaseLikeCount(boardId, memberId);
        Events.raise(new LikePushedEvent(boardId, isNeedToIncrease));
        return isNeedToIncrease;
    }

    private boolean doesNeedToIncreaseLikeCount(final Long boardId, final Long memberId) {
        if (likeStorageRepository.existsByBoardIdAndMemberId(boardId, memberId)) {
            likeStorageRepository.deleteByBoardIdAndMemberId(boardId, memberId);
            return false;
        }

        likeStorageRepository.save(new LikeStorage(boardId, memberId));
        return true;
    }
}