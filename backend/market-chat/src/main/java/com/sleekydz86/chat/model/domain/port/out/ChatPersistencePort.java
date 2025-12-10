package com.sleekydz86.chat.model.domain.port.out;

import com.sleekydz86.chat.model.domain.Chat;

public interface ChatPersistencePort {

    Chat save(Chat chat);
}
