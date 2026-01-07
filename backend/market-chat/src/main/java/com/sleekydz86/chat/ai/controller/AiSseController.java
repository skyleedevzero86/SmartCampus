package com.sleekydz86.chat.ai.controller;

import com.sleekydz86.chat.ai.enums.SSEMsgType;
import com.sleekydz86.chat.ai.infrastructure.sse.SseEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/ai/sse")
@RequiredArgsConstructor
public class AiSseController {

    private final SseEventService sseEventService;

    @GetMapping(path = "/connect", produces = { MediaType.TEXT_EVENT_STREAM_VALUE })
    public SseEmitter connect(@RequestParam String userId) {
        return sseEventService.connect(userId);
    }

    @GetMapping("/sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message) {
        sseEventService.sendMessage(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }
}

