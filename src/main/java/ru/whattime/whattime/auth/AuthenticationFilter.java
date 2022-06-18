package ru.whattime.whattime.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.service.UserService;


import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${application.auth.cookie.name}")
    private String cookieName;

    private final UserService service;

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            String token = getAuthTokenFromCookie(request);

            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            User user = tokenProvider.parseToken(token);

            if (service.getUserById(user.getId()).isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }

        filterChain.doFilter(request, response);
    }

    private String getAuthTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName()) && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
