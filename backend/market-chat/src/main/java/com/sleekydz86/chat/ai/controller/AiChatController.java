package com.sleekydz86.chat.ai.controller;

import com.sleekydz86.chat.ai.application.chat.AiChatApplicationService;
import com.sleekydz86.chat.ai.bean.AiChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatApplicationService aiChatApplicationService;

    @PostMapping("/send")
    public void chat(@RequestBody AiChatRequest request) {
        aiChatApplicationService.streamChat(request);
    }
}

