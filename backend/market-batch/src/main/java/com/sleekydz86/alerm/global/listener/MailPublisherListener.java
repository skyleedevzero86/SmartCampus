package com.sleekydz86.alerm.global.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleekydz86.alerm.batch.infrastructure.mail.mapper.MailStorageMapper;
import com.sleekydz86.alerm.global.exception.DatabaseException;
import com.sleekydz86.server.market.member.domain.auth.event.RegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailPublisherListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final MailStorageMapper mailStorageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            log.debug("Redis 'auth-mail' 채널에서 메시지 수신: {}", messageBody);
            
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
            
            validateProcedureResult(params, event);
        } catch (Exception e) {
            log.error("Redis 'auth-mail' 채널 메시지 처리 중 오류 발생", e);
            throw new DatabaseException("메일 저장소 저장 중 오류가 발생했습니다.", e);
        }
    }

    private void validateProcedureResult(Map<String, Object> params, RegisteredEvent event) {
        Integer affectedRows = (Integer) params.get("affectedRows");
        String resultMessage = (String) params.get("resultMessage");
        
        if (affectedRows == null || affectedRows <= 0) {
            String errorMessage = resultMessage != null && !resultMessage.isEmpty() 
                    ? resultMessage 
                    : "프로시저 실행 실패";
            log.error("메일 저장소 저장 실패 - 회원ID: {}, 이메일: {}, 결과: {}", 
                    event.getMemberId(), event.getEmail(), errorMessage);
            throw new DatabaseException(errorMessage);
        }
        
        log.info("메일 저장소 저장 완료 - 회원ID: {}, 이메일: {}, 결과: {}", 
                event.getMemberId(), event.getEmail(), resultMessage);
    }
}

