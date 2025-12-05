package com.sleekydz86.server.market.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeStorageMapper {

    void save(LikeStorage likeStorage);

    boolean existsByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    void deleteByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}


