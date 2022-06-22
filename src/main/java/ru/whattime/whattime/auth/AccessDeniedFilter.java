package ru.whattime.whattime.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.whattime.whattime.security.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@Order(2)
@RequiredArgsConstructor
public class AccessDeniedFilter extends OncePerRequestFilter {

    // TODO: Regexp
    private static final Set<Pair<String, String>> REQUESTS_TO_FILTER = Set.of(
            Pair.of("/api/v1/events", "POST"),
            Pair.of("/api/v1/events/{var}/intervals", "POST")
    );

    private final SecurityContext securityContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (securityContext.getIdentified() == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return REQUESTS_TO_FILTER.stream()
                .noneMatch(entry -> checkMatches(entry.getFirst(), request.getRequestURI())
                        && entry.getSecond().equals(request.getMethod()));
    }

    private boolean checkMatches(String entry, String request) {
        String regex = entry.replace("{var}", ".+?");

        if (regex.endsWith("/")) {
            regex = regex + "?";
        } else {
            regex = regex + "/?";
        }

        return request.matches(regex);
    }
}
