package com.sleekydz86.server.market.member.ui.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class AuthenticationExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public static Optional<String> extract(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        
        if (token.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(token);
    }
}

