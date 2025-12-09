package com.sleekydz86.server.market.board.infrastructure.mapper;

import com.sleekydz86.server.market.board.domain.LikeStorage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface LikeStorageMapper {

    boolean existsByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    void executeLikeStorageCUD(Map<String, Object> params);
}


