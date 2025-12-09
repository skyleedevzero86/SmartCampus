package com.sleekydz86.alerm.batch.domain.event;

import java.io.Serializable;

public record RegisteredEvent(
        Long memberId,
        String email,
        String nickname,
        Long timestamp
) implements Serializable {
}
