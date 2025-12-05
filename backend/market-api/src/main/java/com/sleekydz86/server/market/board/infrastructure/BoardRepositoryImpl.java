package com.sleekydz86.server.market.board.infrastructure;

import com.sleekydz86.server.market.board.domain.Board;
import com.sleekydz86.server.market.board.domain.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardMapper boardMapper;
    private final BoardQueryRepository boardQueryRepository;

    @Override
    public Board save(final Board board) {
        boardMapper.save(board);
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
        boardMapper.deleteById(boardId);
    }
}
