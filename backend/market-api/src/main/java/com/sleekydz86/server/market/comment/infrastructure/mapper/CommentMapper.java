package com.sleekydz86.server.market.comment.infrastructure.mapper;

import com.sleekydz86.server.market.comment.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    Optional<Comment> findById(@Param("id") Long id);

    void executeCommentCUD(Map<String, Object> params);
}


