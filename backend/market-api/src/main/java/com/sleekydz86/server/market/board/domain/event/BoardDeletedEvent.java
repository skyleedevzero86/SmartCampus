package com.sleekydz86.server.market.board.domain.event;

import com.sleekydz86.server.global.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardDeletedEvent extends Event {

    private final Long boardId;
}
