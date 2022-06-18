package ru.whattime.whattime.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.service.UserService;



import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserService service;

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            String token = tokenProvider.provideToken(request);

            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                User user = parseToken(token);

                if (service.findById(user.getId()).isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private User parseToken(String token) throws IOException {
        String[] parts = token.split("\\.");
        String body = parts[1];

        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(decoder.decode(body), User.class);
    }
}
