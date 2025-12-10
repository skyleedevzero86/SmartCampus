package com.sleekydz86.alerm.batch.infrastructure.schedule;

import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTask;
import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTaskRepository;
import com.sleekydz86.alerm.batch.domain.schedule.TaskStatus;
import com.sleekydz86.alerm.batch.infrastructure.schedule.mapper.ScheduleTaskMapper;
import com.sleekydz86.alerm.global.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ScheduleTaskRepositoryImpl implements ScheduleTaskRepository {

    private final ScheduleTaskMapper scheduleTaskMapper;

    @Override
    public void save(final ScheduleTask scheduleTask) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "C");
            params.put("taskId", scheduleTask.getTaskId());
            params.put("executionTime", scheduleTask.getExecutionTime());
            params.put("status", scheduleTask.getStatus().name());
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            scheduleTaskMapper.executeScheduleTaskCUD(params);
            
            validateProcedureResult(params, "C", scheduleTask.getTaskId());
        } catch (Exception e) {
            log.error("스케줄 작업 저장 실패 - taskId: {}", scheduleTask.getTaskId(), e);
            throw new DatabaseException("스케줄 작업 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public Optional<ScheduleTask> findById(final String taskId) {
        try {
            return Optional.ofNullable(scheduleTaskMapper.findById(taskId));
        } catch (Exception e) {
            log.error("스케줄 작업 조회 실패 - taskId: {}", taskId, e);
            throw new DatabaseException("스케줄 작업 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void updateTaskStatusById(final String taskId, final TaskStatus taskStatus) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "U");
            params.put("taskId", taskId);
            params.put("executionTime", null);
            params.put("status", taskStatus.name());
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            scheduleTaskMapper.executeScheduleTaskCUD(params);
            
            validateProcedureResult(params, "U", taskId);
        } catch (Exception e) {
            log.error("스케줄 작업 상태 업데이트 실패 - taskId: {}, status: {}", taskId, taskStatus, e);
            throw new DatabaseException("스케줄 작업 상태 업데이트 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void deleteByTaskId(final String taskId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "D");
            params.put("taskId", taskId);
            params.put("executionTime", null);
            params.put("status", null);
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            scheduleTaskMapper.executeScheduleTaskCUD(params);
            
            validateProcedureResult(params, "D", taskId);
        } catch (Exception e) {
            log.error("스케줄 작업 삭제 실패 - taskId: {}", taskId, e);
            throw new DatabaseException("스케줄 작업 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateProcedureResult(Map<String, Object> params, String operation, String taskId) {
        Integer affectedRows = (Integer) params.get("affectedRows");
        String resultMessage = (String) params.get("resultMessage");
        
        if (affectedRows == null || affectedRows <= 0) {
            String errorMessage = resultMessage != null && !resultMessage.isEmpty() 
                    ? resultMessage 
                    : String.format("프로시저 실행 실패 (operation: %s, taskId: %s)", operation, taskId);
            throw new DatabaseException(errorMessage);
        }
    }
}
