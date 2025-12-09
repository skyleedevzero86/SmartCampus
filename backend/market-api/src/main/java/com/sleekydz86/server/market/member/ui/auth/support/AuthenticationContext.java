package com.sleekydz86.server.market.member.ui.auth.support;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationContext {

    private static final ThreadLocal<Long> MEMBER_ID = new ThreadLocal<>();

    public void setAuthentication(final Long memberId) {
        MEMBER_ID.set(memberId);
    }

    public void setAnonymous() {
        MEMBER_ID.remove();
    }

    public Optional<Long> getMemberId() {
        return Optional.ofNullable(MEMBER_ID.get());
    }

    public void clear() {
        MEMBER_ID.remove();
    }
}

