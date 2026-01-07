package com.sleekydz86.chat.ai.bean;

import com.sleekydz86.chat.ai.enums.ChatMode;
import lombok.Data;

@Data
public class AiChatRequest {

    private String userId;

    private String message;

    private String botMsgId;

    private ChatMode mode;

}

