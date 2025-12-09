package com.sleekydz86.server.market.board.domain;

import com.sleekydz86.server.market.board.application.dto.BoardFoundResponse;
import com.sleekydz86.server.market.board.application.dto.BoardSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardRepository {

    Board save(Board board);

    Optional<Board> findById(Long id);

    Optional<BoardFoundResponse> findByIdForRead(Long boardId, Long memberId);

    Page<BoardSimpleResponse> findAllBoardsWithPaging(Pageable pageable, Long memberId);

    Optional<Board> findByIdUsingPessimistic(Long id);

    Optional<Board> findBoardWithImages(Long boardId);

    void deleteByBoardId(Long boardId);
}