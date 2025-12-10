package com.sleekydz86.alerm.batch.infrastructure.schedule;

import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTask;
import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTaskRepository;
import com.sleekydz86.alerm.batch.domain.schedule.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ScheduleTaskRepositoryImpl implements ScheduleTaskRepository {

    private final ScheduleTaskJdbcRepository scheduleTaskJdbcRepository;

    @Override
    public void save(final ScheduleTask scheduleTask) {
        scheduleTaskJdbcRepository.save(scheduleTask);
    }

    @Override
    public Optional<ScheduleTask> findById(final String taskId) {
        return scheduleTaskJdbcRepository.findById(taskId);
    }

    @Override
    public void updateTaskStatusById(final String taskId, final TaskStatus taskStatus) {
        scheduleTaskJdbcRepository.updateTaskStatusById(taskId, taskStatus);
    }

    @Override
    public void deleteByTaskId(final String taskId) {
        scheduleTaskJdbcRepository.deleteByTaskId(taskId);
    }
}
