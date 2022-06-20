package ru.whattime.whattime.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    /**
     * Put paths that should be filtered here
     */
    private static final Set<String> URL_PATTERNS = new HashSet<>(Arrays.asList(
        "/api/v1/event"
    ));

    @Value("${application.auth.cookie.name}")
    private String cookieName;

    private final UserService service;

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getAuthTokenFromCookie(request);
        UserDTO user = tokenProvider.parseToken(token);

        if (request.getMethod().equals("POST") && URL_PATTERNS.contains(request.getRequestURI())) {
            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (service.getUserById(user.getId()).isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

        }

        if (user != null) {
            service.identifyUser(user);
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
