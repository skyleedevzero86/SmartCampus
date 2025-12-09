package com.sleekydz86.server.market.board.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeStorage extends BaseEntity {

    private Long id;

    private Long boardId;

    private Long memberId;

    public LikeStorage(final Long boardId, final Long memberId) {
        this.boardId = boardId;
        this.memberId = memberId;
    }
}

