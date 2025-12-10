package com.sleekydz86.alerm.batch.application;

import com.sleekydz86.alerm.batch.domain.event.ScheduledEvent;
import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTask;
import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTaskRepository;
import com.sleekydz86.alerm.batch.domain.schedule.TaskStatus;
import com.sleekydz86.alerm.global.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduleTaskEventHandler {

    private static final String RUNNING_EVERY_FIVE_SECOND = "0/5 * * * * *";
    private static final int THREAD_COUNT = 1;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Queue<ScheduledEvent> scheduledTasks = new ConcurrentLinkedDeque<>();
    private final ScheduleTaskRepository scheduleTaskRepository;

    @EventListener
    public void addScheduleTask(final ScheduledEvent event) {
        try {
            this.scheduledTasks.add(event);
            log.debug("스케줄 작업 추가 - taskId: {}", event.taskId());
        } catch (Exception e) {
            log.error("스케줄 작업 추가 실패 - taskId: {}", event.taskId(), e);
            throw new ScheduleException("스케줄 작업 추가 중 오류가 발생했습니다.", e);
        }
    }

    @Scheduled(cron = RUNNING_EVERY_FIVE_SECOND)
    public void runScheduleTask() {
        if (!canWorkTask()) {
            return;
        }

        executorService.execute(() -> {
            try {
                runTask(scheduledTasks.poll());
            } catch (Exception e) {
                log.error("스케줄 작업 실행 중 오류 발생", e);
            }
        });
    }

    private boolean canWorkTask() {
        return !scheduledTasks.isEmpty()
                || isRunning.compareAndSet(false, true);
    }

    @Transactional(rollbackFor = Exception.class)
    private void runTask(final ScheduledEvent task) {
        if (task == null) {
            return;
        }

        String taskId = task.taskId();
        LocalDateTime executionTime = task.date();

        try {
            if (doesAlreadyProcess(taskId)) {
                log.info("스케줄 작업 이미 실행 중 - taskId: {}, 실행 시간: {}", taskId, executionTime);
                return;
            }

            ScheduleTask scheduleTask = ScheduleTask.createRunningTask(taskId, executionTime);
            scheduleTaskRepository.save(scheduleTask);

            task.job().run();
            scheduleTaskRepository.deleteByTaskId(task.taskId());
            log.info("스케줄 작업 성공 - taskId: {}", taskId);
        } catch (Exception e) {
            log.error("스케줄 작업 실행 중 오류 발생 - taskId: {}", taskId, e);
            handleTaskError(task, taskId);
            throw new ScheduleException("스케줄 작업 실행 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void handleTaskError(final ScheduledEvent task, final String taskId) {
        try {
            ScheduleTask scheduleTask = scheduleTaskRepository.findById(taskId)
                    .orElseThrow(() -> new ScheduleException("스케줄 작업을 찾을 수 없습니다 - taskId: " + taskId));
            
            scheduleTask.error();
            scheduleTaskRepository.updateTaskStatusById(scheduleTask.getTaskId(), TaskStatus.ERROR);
            scheduledTasks.add(task);
            log.warn("스케줄 작업 재시도 대기열에 추가 - taskId: {}", taskId);
        } catch (Exception e) {
            log.error("스케줄 작업 오류 처리 중 예외 발생 - taskId: {}", taskId, e);
        }
    }

    private boolean doesAlreadyProcess(String jobId) {
        try {
            return scheduleTaskRepository.findById(jobId)
                    .map(scheduleTask -> !scheduleTask.canWork())
                    .orElse(false);
        } catch (Exception e) {
            log.error("스케줄 작업 조회 중 오류 발생 - jobId: {}", jobId, e);
            return false;
        }
    }
}
