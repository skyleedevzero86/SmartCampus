package com.sleekydz86.server.member.auth;

public interface TokenProvider {

    String create(final Long id);

    Long extract(final String token);
}

