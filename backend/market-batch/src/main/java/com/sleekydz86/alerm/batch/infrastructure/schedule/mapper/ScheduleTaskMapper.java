package com.sleekydz86.alerm.batch.infrastructure.schedule.mapper;

import com.sleekydz86.alerm.batch.domain.schedule.ScheduleTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ScheduleTaskMapper {
    
    void executeScheduleTaskCUD(Map<String, Object> params);
    
    ScheduleTask findById(@Param("taskId") String taskId);
}

