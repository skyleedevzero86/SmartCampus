package com.sleekydz86.alerm.batch.infrastructure.mail;

import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.MailStorageRepository;
import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import com.sleekydz86.alerm.batch.infrastructure.mail.mapper.MailStorageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MailStorageRepositoryImpl implements MailStorageRepository {

    private final MailStorageMapper mailStorageMapper;
    private final MailStorageJdbcRepository mailStorageJdbcRepository;

    @Override
    public void save(final MailStorage mailStorage) {
        mailStorageMapper.save(mailStorage);
    }

    @Override
    public List<MailStorage> findAllByNotDone() {
        return mailStorageMapper.findAllByMailStatus(MailStatus.FAIL);
    }

    @Override
    public void deleteAllByDoneMails() {
        mailStorageJdbcRepository.deleteAllByMailStatus(MailStatus.DONE);
    }
}
