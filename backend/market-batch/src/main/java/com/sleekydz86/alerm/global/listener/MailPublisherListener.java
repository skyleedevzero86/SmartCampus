package com.sleekydz86.alerm.global.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleekydz86.alerm.batch.infrastructure.mail.mapper.MailStorageMapper;
import com.sleekydz86.server.market.member.domain.auth.event.RegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
            
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "C");
            params.put("id", null);
            params.put("memberId", event.getMemberId());
            params.put("email", event.getEmail());
            params.put("nickname", event.getNickname());
            params.put("mailStatus", "WAIT");
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            mailStorageMapper.executeMailStorageCUD(params);
            
            String resultMessage = (String) params.get("resultMessage");
            Integer affectedRows = (Integer) params.get("affectedRows");
            
            if (affectedRows != null && affectedRows > 0) {
                log.info("메일 저장소 저장 완료 - 회원ID: {}, 이메일: {}, 결과: {}", 
                        event.getMemberId(), event.getEmail(), resultMessage);
            } else {
                log.warn("메일 저장소 저장 실패 - 회원ID: {}, 이메일: {}, 결과: {}", 
                        event.getMemberId(), event.getEmail(), resultMessage);
            }
            
        } catch (Exception e) {
            log.error("Redis 'auth-mail' 채널 메시지 처리 중 오류 발생", e);
        }
    }
}

