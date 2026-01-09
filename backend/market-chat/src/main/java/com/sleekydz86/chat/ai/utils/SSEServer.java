package com.sleekydz86.chat.ai.utils;

import com.sleekydz86.chat.ai.enums.SSEMsgType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class SSEServer {

    private static final Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onTimeout(timeoutCallback(userId));
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));

        sseClients.put(userId, sseEmitter);

        return sseEmitter;
    }

    public static void close(String userId) {
        SseEmitter emitter = sseClients.get(userId);
        if (emitter != null) {
            emitter.complete();
        }
    }

    public static void sendMsg(String userId, String message, SSEMsgType msgType) {
        if (CollectionUtils.isEmpty(sseClients)) {
            return;
        }

        if (sseClients.containsKey(userId)) {
            SseEmitter sseEmitter = sseClients.get(userId);
            sendEmitterMessage(sseEmitter, userId, message, msgType);
        }
    }

    public static void sendMsgToAllUsers(String message) {
        if (CollectionUtils.isEmpty(sseClients)) {
            return;
        }

        sseClients.forEach((userId, sseEmitter) -> {
            sendEmitterMessage(sseEmitter, userId, message, SSEMsgType.MESSAGE);
        });
    }

    private static void sendEmitterMessage(SseEmitter sseEmitter,
                                          String userId,
                                          String message,
                                          SSEMsgType msgType) {
        SseEmitter.SseEventBuilder msgEvent = SseEmitter.event()
                .id(userId)
                .data(message)
                .name(msgType.type);

        try {
            sseEmitter.send(msgEvent);
        } catch (IOException e) {
            log.error("SSE 메시지 전송 오류, 사용자ID: {}, 오류: {}", userId, e.getMessage());
            close(userId);
        }
    }

    public static Runnable timeoutCallback(String userId) {
        return () -> {
            log.info("SSE 타임아웃...");
            remove(userId);
        };
    }

    public static Runnable completionCallback(String userId) {
        return () -> {
            log.info("SSE 완료...");
            remove(userId);
        };
    }

    public static Consumer<Throwable> errorCallback(String userId) {
        return throwable -> {
            log.info("SSE 예외...");
            remove(userId);
        };
    }

    public static void remove(String userId) {
        sseClients.remove(userId);
        log.info("사용자 제거: {}", userId);
    }
}

