package com.sleekydz86.server.market.comment.application;

import com.sleekydz86.server.market.comment.domain.CommentRepository;
import com.sleekydz86.server.market.comment.domain.dto.CommentSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentQueryService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentSimpleResponse> findAllCommentsByBoardId(
            final Long boardId,
            final Long memberId,
            final Long commentId,
            final int pageSize
    ) {
        return commentRepository.findAllCommentsByBoardId(boardId, memberId, commentId, pageSize);
    }
}