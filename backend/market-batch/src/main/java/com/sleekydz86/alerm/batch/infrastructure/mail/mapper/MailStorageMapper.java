package com.sleekydz86.alerm.batch.infrastructure.mail.mapper;

import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailStorageMapper {
    void save(MailStorage mailStorage);
    List<MailStorage> findAllByMailStatus(MailStatus mailStatus);
}

