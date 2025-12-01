package com.sleekydz86.server.global.exception.exceptions.member;

public class MemberAlreadyExistedException extends RuntimeException {

    public MemberAlreadyExistedException() {
        super("이미 존재하는 Email입니다.");
    }
}
