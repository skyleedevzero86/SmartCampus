package com.sleekydz86.alerm.batch.application;

import com.sleekydz86.alerm.batch.domain.mail.MailSender;
import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.MailStorageRepository;
import com.sleekydz86.alerm.global.exception.MailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private static final int THREAD_COUNT = 4;

    private final MailStorageRepository mailStorageRepository;
    private final MailSender mailSender;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Transactional(rollbackFor = Exception.class)
    public void sendMail(final MailStorage mailStorage) {
        try {
            mailSender.pushMail(mailStorage.getReceiverEmail(), mailStorage.getId(), mailStorage.getReceiverNickname());
            mailStorage.updateStatusDone();
            mailStorageRepository.save(mailStorage);
            log.info("메일 발송 성공 - 회원ID: {}, 닉네임: {}", mailStorage.getReceiverId(), mailStorage.getReceiverNickname());
        } catch (Exception e) {
            log.error("메일 발송 실패 - 회원ID: {}, 닉네임: {}", 
                    mailStorage.getReceiverId(), mailStorage.getReceiverNickname(), e);
            saveFailureSendMail(mailStorage, e);
            throw new MailException("메일 발송 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void saveFailureSendMail(final MailStorage mailStorage, final Exception exception) {
        try {
            mailStorage.updateStatusFail();
            mailStorageRepository.save(mailStorage);
            log.warn("메일 발송 실패 상태 저장 완료 - 회원ID: {}, 닉네임: {}", 
                    mailStorage.getReceiverId(), mailStorage.getReceiverNickname());
        } catch (Exception e) {
            log.error("메일 발송 실패 상태 저장 중 오류 발생 - 회원ID: {}, 닉네임: {}", 
                    mailStorage.getReceiverId(), mailStorage.getReceiverNickname(), e);
            throw new MailException("메일 발송 실패 상태 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void resendMail() {
        if (!isRunning.compareAndSet(false, true)) {
            log.warn("메일 재전송 작업이 이미 실행 중입니다.");
            return;
        }

        try {
            List<MailStorage> sendFailureMails = mailStorageRepository.findAllByNotDone();
            
            if (sendFailureMails.isEmpty()) {
                log.info("재전송할 실패 메일이 없습니다.");
                return;
            }

            resendFailureMailUsingExecutors(sendFailureMails);
            log.info("실패 메일 재전송 완료 - 건수: {}", sendFailureMails.size());
        } catch (Exception e) {
            log.error("메일 재전송 중 오류 발생", e);
            throw new MailException("메일 재전송 중 오류가 발생했습니다.", e);
        } finally {
            isRunning.set(false);
        }
    }

    private void resendFailureMailUsingExecutors(final List<MailStorage> sendFailureMails) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        try {
            sendFailureMails.forEach(failureMail -> 
                executorService.submit(() -> {
                    try {
                        sendMail(failureMail);
                    } catch (Exception e) {
                        log.error("개별 메일 재전송 실패 - 회원ID: {}", failureMail.getReceiverId(), e);
                    }
                })
            );
        } finally {
            executorService.shutdown();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSuccessMails() {
        try {
            mailStorageRepository.deleteAllByDoneMails();
            log.info("완료된 메일 삭제 완료");
        } catch (Exception e) {
            log.error("완료된 메일 삭제 중 오류 발생", e);
            throw new MailException("완료된 메일 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
