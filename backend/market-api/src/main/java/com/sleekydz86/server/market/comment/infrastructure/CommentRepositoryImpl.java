package com.sleekydz86.server.market.comment.infrastructure;

import com.sleekydz86.server.market.comment.domain.Comment;
import com.sleekydz86.server.market.comment.domain.CommentRepository;
import com.sleekydz86.server.market.comment.domain.dto.CommentSimpleResponse;
import com.sleekydz86.server.market.comment.infrastructure.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;
    private final CommentJdbcRepository commentJdbcRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public Comment save(final Comment comment) {
        commentMapper.save(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(final Long id) {
        return commentMapper.findById(id);
    }

    public List<CommentSimpleResponse> findAllCommentsByBoardId(final Long boardId, final Long memberId, final Long commentId, final int pageSize) {
        return commentQueryRepository.findAllWithPaging(boardId, memberId, commentId, pageSize);
    }

    @Override
    public void deleteById(final Long id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void deleteAllByBoardId(final Long boardId) {
        commentJdbcRepository.deleteAllCommentsByBoardId(boardId);
    }
}
