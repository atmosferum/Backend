package ru.meettime.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.meettime.model.User;

@Component
@RequestScope
public class SecurityContext {

    @Getter
    @Setter
    private User identified;
}
