package com.sleekydz86.alerm.batch.domain.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "taskId")
@Builder
public class ScheduleTask {

    private String taskId;

    private LocalDateTime executionTime;

    private TaskStatus status;

    public static ScheduleTask createRunningTask(final String jobId, final LocalDateTime executionTime) {
        return new ScheduleTask(jobId, executionTime, TaskStatus.RUNNING);
    }

    public void error() {
        this.status = TaskStatus.ERROR;
    }

    public boolean canWork() {
        return this.status.canWork();
    }
}
