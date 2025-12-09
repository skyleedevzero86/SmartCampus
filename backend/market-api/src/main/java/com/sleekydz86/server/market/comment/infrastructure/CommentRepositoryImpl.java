package com.sleekydz86.server.market.comment.infrastructure;

import com.sleekydz86.server.market.comment.domain.Comment;
import com.sleekydz86.server.market.comment.domain.CommentRepository;
import com.sleekydz86.server.market.comment.domain.dto.CommentSimpleResponse;
import com.sleekydz86.server.market.comment.infrastructure.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;
    private final CommentJdbcRepository commentJdbcRepository;

    @Override
    public Comment save(final Comment comment) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("content", comment.getContent());
        params.put("writerId", comment.getWriterId());
        params.put("boardId", comment.getBoardId());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        commentMapper.executeCommentCUD(params);
        return comment;
    }

    @Override
    public Optional<Comment> findById(final Long id) {
        return commentMapper.findById(id);
    }

    public List<CommentSimpleResponse> findAllCommentsByBoardId(final Long boardId, final Long memberId, final Long commentId, final int pageSize) {
        throw new UnsupportedOperationException("findAllCommentsByBoardId needs to be implemented with MyBatis");
    }

    @Override
    public void deleteById(final Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", id);
        params.put("content", null);
        params.put("writerId", null);
        params.put("boardId", null);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        commentMapper.executeCommentCUD(params);
    }

    @Override
    public void deleteAllByBoardId(final Long boardId) {
        commentJdbcRepository.deleteAllCommentsByBoardId(boardId);
    }
}
