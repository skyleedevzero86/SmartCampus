package com.sleekydz86.server.market.board.infrastructure.mapper;

import com.sleekydz86.server.market.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface BoardMapper {

    Optional<Board> findById(@Param("id") Long id);

    Optional<Board> findByIdUsingPessimistic(@Param("id") Long id);

    Optional<Board> findBoardWithImages(@Param("boardId") Long boardId);

    void executeBoardCUD(Map<String, Object> params);
}


