package com.sleekydz86.alerm.batch.domain.event;

import java.time.LocalDateTime;

public record ScheduledEvent(
        Runnable job,
        String taskId,
        LocalDateTime date
) {
}
