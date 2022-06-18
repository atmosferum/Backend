package ru.whattime.whattime.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.service.UserService;



import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class IdentificationFilter implements Filter {

    @Value("${application.auth.cookie.name}")
    private String cookieName;

    private final UserService service;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals("POST")) {
            String token = extractUserTokenFromCookies(request);



            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                User user = parseToken(token);
                System.out.println(user.getId());

                if (service.getUserById(user.getId()).isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractUserTokenFromCookies(HttpServletRequest request) {
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

    private boolean userInDatabase(String token) {
        return false;
    }

    private User parseToken(String token) throws IOException {
        String[] parts = token.split("\\.");
        String body = parts[1];

        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(decoder.decode(body), User.class);
    }
}
