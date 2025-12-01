package com.sleekydz86.server.global.exception.exceptions.member;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("Member를 찾을 수 없습니다.");
    }
}
