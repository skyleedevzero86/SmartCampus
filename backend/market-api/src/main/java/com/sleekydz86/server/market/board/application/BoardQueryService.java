package com.sleekydz86.server.market.board.application;

import com.sleekydz86.server.market.board.application.dto.BoardFoundResponse;
import com.sleekydz86.server.market.board.application.dto.BoardSimpleResponse;
import com.sleekydz86.server.market.board.application.dto.BoardsSimpleResponse;
import com.sleekydz86.server.market.board.domain.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardQueryService {

    private final BoardRepository boardRepository;

    public BoardsSimpleResponse findAllBoards(final Pageable pageable, final Long memberId) {
        Page<BoardSimpleResponse> response = boardRepository.findAllBoardsWithPaging(pageable, memberId);
        return BoardsSimpleResponse.of(response, pageable);
    }

    public BoardFoundResponse findBoardById(final Long boardId, final Long memberId) {
        return boardRepository.findByIdForRead(boardId, memberId)
                .orElseThrow(BoardNotFoundException::new);
    }
}