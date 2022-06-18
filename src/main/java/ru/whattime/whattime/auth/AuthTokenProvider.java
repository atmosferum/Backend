package ru.whattime.whattime.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class AuthTokenProvider {
    @Value("${application.auth.cookie.name}")
    private String cookieName;

    public String provideToken(HttpServletRequest request) {
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
