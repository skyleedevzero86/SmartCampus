package com.sleekydz86.server.market.board.infrastructure;

import com.sleekydz86.server.market.board.application.dto.BoardFoundResponse;
import com.sleekydz86.server.market.board.application.dto.BoardSimpleResponse;
import com.sleekydz86.server.market.board.domain.Board;
import com.sleekydz86.server.market.board.domain.BoardRepository;
import com.sleekydz86.server.market.board.infrastructure.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardMapper boardMapper;
    private final BoardQueryRepository boardQueryRepository;

    @Override
    public Board save(final Board board) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("title", board.getPost().getTitle());
        params.put("content", board.getPost().getContent());
        params.put("writerId", board.getWriterId());
        params.put("likeCount", board.getLikeCount().getValue());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        boardMapper.executeBoardCUD(params);
        return board;
    }

    @Override
    public Optional<Board> findById(final Long id) {
        return boardMapper.findById(id);
    }

    @Override
    public Optional<BoardFoundResponse> findByIdForRead(final Long boardId, final Long memberId) {
        return boardQueryRepository.findById(boardId, memberId);
    }

    @Override
    public Page<BoardSimpleResponse> findAllBoardsWithPaging(final Pageable pageable, final Long memberId) {
        return boardQueryRepository.findAllBoard(pageable, memberId);
    }

    @Override
    public Optional<Board> findByIdUsingPessimistic(final Long id) {
        return boardMapper.findByIdUsingPessimistic(id);
    }

    @Override
    public Optional<Board> findBoardWithImages(final Long boardId) {
        return boardMapper.findBoardWithImages(boardId);
    }

    @Override
    public void deleteByBoardId(final Long boardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", boardId);
        params.put("title", null);
        params.put("content", null);
        params.put("writerId", null);
        params.put("likeCount", null);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        boardMapper.executeBoardCUD(params);
    }
}
