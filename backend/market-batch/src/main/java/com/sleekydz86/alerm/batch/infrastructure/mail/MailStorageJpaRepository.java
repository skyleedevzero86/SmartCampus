package com.sleekydz86.alerm.batch.infrastructure.mail;

import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailStorageJpaRepository extends JpaRepository<MailStorage, Long> {

    MailStorage save(final MailStorage mailStorage);

    List<MailStorage> findAllByMailStatus(final MailStatus mailStatus);
}
