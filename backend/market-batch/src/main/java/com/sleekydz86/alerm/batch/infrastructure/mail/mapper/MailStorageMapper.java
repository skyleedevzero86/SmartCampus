package com.sleekydz86.alerm.batch.infrastructure.mail.mapper;

import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MailStorageMapper {
    void executeMailStorageCUD(Map<String, Object> params);
    
    MailStorage findById(@Param("id") Long id);
    
    List<MailStorage> findAllByMailStatus(@Param("mailStatus") MailStatus mailStatus);
}

