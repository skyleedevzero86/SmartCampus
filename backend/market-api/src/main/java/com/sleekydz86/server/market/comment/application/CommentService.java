package com.sleekydz86.server.market.comment.application;

import com.sleekydz86.server.global.exception.exceptions.CommentNotFoundException;
import com.sleekydz86.server.market.comment.application.dto.CommentCreateRequest;
import com.sleekydz86.server.market.comment.application.dto.CommentPatchRequest;
import com.sleekydz86.server.market.comment.domain.Comment;
import com.sleekydz86.server.market.comment.domain.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void createComment(
            final Long memberId,
            final Long boardId,
            final CommentCreateRequest request
    ) {
        Comment comment = Comment.builder()
                .boardId(boardId)
                .writerId(memberId)
                .content(request.comment())
                .build();

        commentRepository.save(comment);
    }

    public void patchCommentById(
            final Long memberId,
            final Long commentId,
            final CommentPatchRequest request
    ) {
        Comment comment = findComment(commentId);
        comment.update(request.comment(), memberId);
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    public void deleteCommentById(final Long memberId, final Long commentId) {
        Comment comment = findComment(commentId);
        comment.validateWriterId(memberId);

        commentRepository.deleteById(commentId);
    }

    public void deleteAllCommentsByBoardId(final Long deletedBoardId) {
        commentRepository.deleteAllByBoardId(deletedBoardId);
    }
}
