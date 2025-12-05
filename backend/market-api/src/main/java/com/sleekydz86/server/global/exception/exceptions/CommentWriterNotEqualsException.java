package com.sleekydz86.server.global.exception.exceptions;

public class CommentWriterNotEqualsException extends RuntimeException {

    public CommentWriterNotEqualsException() {
        super("글쓴이가 일치하지 않습니다.");
    }
}
