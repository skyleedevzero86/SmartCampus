package com.sleekydz86.server.market.board.infrastructure;

import com.sleekydz86.server.market.board.domain.LikeStorage;
import com.sleekydz86.server.market.board.domain.LikeStorageRepository;
import com.sleekydz86.server.market.board.infrastructure.mapper.LikeStorageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class LikeStorageRepositoryImpl implements LikeStorageRepository {

    private final LikeStorageMapper likeStorageMapper;
    private final LikeStorageJpaRepository likeStorageJpaRepository;

    @Override
    public LikeStorage save(final LikeStorage likeStorage) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("boardId", likeStorage.getBoardId());
        params.put("memberId", likeStorage.getMemberId());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        likeStorageMapper.executeLikeStorageCUD(params);
        return likeStorage;
    }

    @Override
    public boolean existsByBoardIdAndMemberId(final Long boardId, final Long memberId) {
        return likeStorageJpaRepository.existsByBoardIdAndMemberId(boardId, memberId);
    }

    @Override
    public void deleteByBoardIdAndMemberId(final Long boardId, final Long memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", null);
        params.put("boardId", boardId);
        params.put("memberId", memberId);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        likeStorageMapper.executeLikeStorageCUD(params);
    }
}
