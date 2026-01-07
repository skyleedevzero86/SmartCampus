package com.sleekydz86.server.global.exception;

import com.sleekydz86.server.global.exception.exceptions.auth.*;
import com.sleekydz86.server.global.exception.exceptions.member.MemberAlreadyExistedException;
import com.sleekydz86.server.global.exception.exceptions.member.MemberAuthInvalidException;
import com.sleekydz86.server.global.exception.exceptions.member.MemberNotFoundException;
import com.sleekydz86.server.global.exception.exceptions.member.PasswordNotMatchedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(final RoleNotFoundException exception) {
        return getResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(MemberAlreadyExistedException.class)
    public ResponseEntity<String> handleMemberAlreadyExistedException(final MemberAlreadyExistedException exception) {
        return getResponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(final MemberNotFoundException exception) {
        return getResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<String> handlePasswordNotMatchedException(final PasswordNotMatchedException exception) {
        return getResponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(MemberAuthInvalidException.class)
    public ResponseEntity<String> handleMemberAuthInvalidException(final MemberAuthInvalidException exception) {
        return getResponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(SignatureInvalidException.class)
    public ResponseEntity<String> handleSignatureInvalidException(final SignatureInvalidException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(TokenFormInvalidException.class)
    public ResponseEntity<String> handleTokenFormInvalidException(final TokenFormInvalidException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<String> handleExpiredTokenException(final ExpiredTokenException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(UnsupportedTokenException.class)
    public ResponseEntity<String> handleUnsupportedTokenException(final UnsupportedTokenException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<String> handleTokenInvalidException(final TokenInvalidException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(LoginInvalidException.class)
    public ResponseEntity<String> handleLoginInvalidException(final LoginInvalidException exception) {
        return getResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    private ResponseEntity<String> getResponse(final HttpStatus httpStatus, final Exception exception) {
        return ResponseEntity.status(httpStatus)
                .body(exception.getMessage());
    }
}
