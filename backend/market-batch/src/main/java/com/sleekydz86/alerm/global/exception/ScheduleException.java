package com.sleekydz86.alerm.global.exception;

public class ScheduleException extends RuntimeException {

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}

