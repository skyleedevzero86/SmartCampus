package com.sleekydz86.alerm.batch.application;

import com.sleekydz86.alerm.batch.domain.event.RegisteredEvent;
import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.global.exception.MailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailEventHandler {

    private final MailService mailService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void sendMail(final RegisteredEvent event) {
        try {
            MailStorage mailStorage = MailStorage.createDefault(event.memberId(), event.email(), event.nickname());
            mailService.sendMail(mailStorage);
        } catch (Exception e) {
            log.error("메일 이벤트 처리 중 오류 발생 - 회원ID: {}, 이메일: {}", 
                    event.memberId(), event.email(), e);
            throw new MailException("메일 이벤트 처리 중 오류가 발생했습니다.", e);
        }
    }
}