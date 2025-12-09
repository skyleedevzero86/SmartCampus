package com.sleekydz86.alerm.batch.domain.schedule;

public enum TaskStatus {

    WAITING,
    RUNNING,
    DONE,
    ERROR;

    public boolean canWork() {
        return this == TaskStatus.WAITING
                || this == TaskStatus.ERROR;
    }
}
