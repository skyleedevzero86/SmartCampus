package com.sleekydz86.chat.ai.enums;

public enum SSEMsgType {

    MESSAGE("message", "단일 전송 일반 메시지"),
    ADD("add", "메시지 추가, 스트리밍 푸시에 적합"),
    FINISH("finish", "메시지 전송 완료"),
    CUSTOM_EVENT("custom_event", "사용자 정의 메시지 타입"),
    DONE("done", "메시지 전송 완료");

    public final String type;
    public final String value;

    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }

}

