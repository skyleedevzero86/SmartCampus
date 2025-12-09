package com.sleekydz86.server.market.board.infrastructure;

import com.sleekydz86.server.market.board.application.dto.BoardFoundResponse;
import com.sleekydz86.server.market.board.application.dto.BoardSimpleResponse;
import com.sleekydz86.server.market.board.infrastructure.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardQueryRepository {

    private final BoardMapper boardMapper;

    public Page<BoardSimpleResponse> findAllBoard(final Pageable pageable, final Long memberId) {
        List<BoardSimpleResponse> results = boardMapper.findAllBoard(
                pageable.getOffset(),
                pageable.getPageSize(),
                memberId
        );
        long total = boardMapper.countAllBoard();
        return new PageImpl<>(results, pageable, total);
    }

    public Optional<BoardFoundResponse> findById(final Long boardId, final Long memberId) {
        return boardMapper.findBoardById(boardId, memberId);
    }
}
