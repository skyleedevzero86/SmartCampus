package com.sleekydz86.server.market.board.infrastructure.mapper;

import com.sleekydz86.server.market.board.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BoardMapper {

    void save(Board board);

    Optional<Board> findById(@Param("id") Long id);

    Optional<Board> findByIdUsingPessimistic(@Param("id") Long id);

    Optional<Board> findBoardWithImages(@Param("boardId") Long boardId);

    void deleteById(@Param("id") Long id);
}


