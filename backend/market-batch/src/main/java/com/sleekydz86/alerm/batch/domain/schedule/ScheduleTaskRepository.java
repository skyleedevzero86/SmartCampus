package com.sleekydz86.alerm.batch.domain.schedule;

import java.util.Optional;

public interface ScheduleTaskRepository {

    void save(ScheduleTask scheduleTask);

    Optional<ScheduleTask> findById(String jobId);

    void updateTaskStatusById(String taskId, TaskStatus taskStatus);

    void deleteByTaskId(String taskId);
}
