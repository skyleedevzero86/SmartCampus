package com.sleekydz86.alerm.batch.application;

import com.sleekydz86.alerm.batch.domain.event.ScheduledEvent;
import com.sleekydz86.alerm.global.event.Events;
import com.sleekydz86.alerm.global.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ScheduleCreateService {

    private static final String RUNNING_EVERY_TWENTY_MINUTE = "0 */20 * * * *";
    private static final String RUNNING_EVERY_THIRTY_MINUTE = "0 */30 * * * *";

    private final MailService mailService;

    @Scheduled(cron = RUNNING_EVERY_TWENTY_MINUTE)
    public void resendMail() {
        try {
            Events.raise(new ScheduledEvent(mailService::resendMail, "resendMail", LocalDateTime.now()));
            log.info("재전송 스케줄링 이벤트 발생");
        } catch (Exception e) {
            log.error("재전송 스케줄링 이벤트 발생 중 오류", e);
            throw new ScheduleException("재전송 스케줄링 이벤트 발생 중 오류가 발생했습니다.", e);
        }
    }

    @Scheduled(cron = RUNNING_EVERY_THIRTY_MINUTE)
    public void deleteSendSuccessMails() {
        try {
            Events.raise(new ScheduledEvent(mailService::deleteSuccessMails, "deleteSendSuccessMail", LocalDateTime.now()));
            log.info("완료된 메일 삭제 스케줄링 이벤트 발생");
        } catch (Exception e) {
            log.error("완료된 메일 삭제 스케줄링 이벤트 발생 중 오류", e);
            throw new ScheduleException("완료된 메일 삭제 스케줄링 이벤트 발생 중 오류가 발생했습니다.", e);
        }
    }
}
