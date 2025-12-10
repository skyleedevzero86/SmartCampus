package com.sleekydz86.chat.model.domain.port;

import com.sleekydz86.chat.model.domain.Chat;

public interface ChatPersistencePort {

    Chat save(Chat chat);
}
