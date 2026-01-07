package com.sleekydz86.chat.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiChatResponse {

    private String message;

    private String botMsgId;

}

