package com.sleekydz86.alerm.global.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.vo.Receiver;
import com.sleekydz86.alerm.batch.infrastructure.mail.mapper.MailStorageMapper;
import com.sleekydz86.server.market.member.domain.auth.event.RegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailPublisherListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final MailStorageMapper mailStorageMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Redis 'auth-mail' 채널에서 메시지 수신: {}", messageBody);
            
            RegisteredEvent event = objectMapper.readValue(messageBody, RegisteredEvent.class);
            log.info("RegisteredEvent 역직렬화 완료 - 회원ID: {}, 이메일: {}, 닉네임: {}", 
                    event.getMemberId(), event.getEmail(), event.getNickname());
            
            Receiver receiver = new Receiver(
                    event.getMemberId(),
                    event.getEmail(),
                    event.getNickname()
            );
            
            MailStorage mailStorage = MailStorage.create(receiver);
            mailStorageMapper.save(mailStorage);
            
            log.info("메일 저장소 저장 완료 - 회원ID: {}, 이메일: {}", 
                    event.getMemberId(), event.getEmail());
            
        } catch (Exception e) {
            log.error("Redis 'auth-mail' 채널 메시지 처리 중 오류 발생", e);
        }
    }
}

