package com.sleekydz86.alerm.batch.application;

import com.sleekydz86.alerm.batch.domain.event.RegisteredEvent;
import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailEventHandler {

    private final MailService mailService;

    @EventListener
    public void sendMail(final RegisteredEvent event) {
        MailStorage mailStorage = MailStorage.createDefault(event.memberId(), event.email(), event.nickname());
        mailService.sendMail(mailStorage);
    }
}