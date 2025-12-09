package com.sleekydz86.alerm.global.event;

import lombok.Getter;

@Getter
public abstract class Event {

    private final Long timestamp;

    protected Event() {
        this.timestamp = System.currentTimeMillis();
    }
}
