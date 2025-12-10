package com.sleekydz86.server.market.member.ui.auth.interceptor;

import com.sleekydz86.server.global.exception.exceptions.auth.LoginInvalidException;
import com.sleekydz86.server.market.member.domain.auth.TokenProvider;
import com.sleekydz86.server.market.member.ui.auth.support.AuthenticationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.sleekydz86.server.market.member.ui.auth.interceptor.AuthenticationExtractor.extract;

@RequiredArgsConstructor
@Component
public class LoginValidCheckerInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final AuthenticationContext authenticationContext;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        String token = extract(request)
                .orElseThrow(LoginInvalidException::new);

        Long memberId = tokenProvider.extract(token);
        authenticationContext.setAuthentication(memberId);

        return true;
    }
}