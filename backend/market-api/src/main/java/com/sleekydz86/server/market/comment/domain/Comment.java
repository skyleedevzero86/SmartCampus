package com.sleekydz86.server.market.comment.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import com.sleekydz86.server.global.exception.exceptions.CommentWriterNotEqualsException;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    private Long id;

    private String content;

    private Long writerId;

    private Long boardId;

    public void update(final String comment, final Long writerId) {
        validateWriterId(writerId);
        this.content = comment;
    }

    public void validateWriterId(final Long writerId) {
        if (!this.writerId.equals(writerId)) {
            throw new CommentWriterNotEqualsException();
        }
    }
}
