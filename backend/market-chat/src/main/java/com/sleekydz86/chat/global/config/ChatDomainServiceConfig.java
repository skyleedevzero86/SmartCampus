package com.sleekydz86.chat.global.config;

import com.sleekydz86.chat.model.domain.port.out.ChattingRoomPersistencePort;
import com.sleekydz86.chat.model.domain.service.ChattingRoomDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatDomainServiceConfig {

    @Bean
    public ChattingRoomDomainService chattingRoomDomainService(
            ChattingRoomPersistencePort chattingRoomPersistencePort
    ) {
        return new ChattingRoomDomainService(chattingRoomPersistencePort);
    }
}