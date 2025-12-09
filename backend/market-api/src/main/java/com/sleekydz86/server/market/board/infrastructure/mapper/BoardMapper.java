package com.sleekydz86.server.market.board.infrastructure.mapper;

import com.sleekydz86.server.market.board.application.dto.BoardFoundResponse;
import com.sleekydz86.server.market.board.application.dto.BoardSimpleResponse;
import com.sleekydz86.server.market.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface BoardMapper {

    Optional<Board> findById(@Param("id") Long id);

    Optional<Board> findByIdUsingPessimistic(@Param("id") Long id);

    Optional<Board> findBoardWithImages(@Param("boardId") Long boardId);

    void executeBoardCUD(Map<String, Object> params);

    List<BoardSimpleResponse> findAllBoard(@Param("offset") long offset, @Param("limit") int limit, @Param("memberId") Long memberId);

    long countAllBoard();

    Optional<BoardFoundResponse> findBoardById(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}


