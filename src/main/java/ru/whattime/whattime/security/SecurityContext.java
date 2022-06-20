package ru.whattime.whattime.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.whattime.whattime.model.User;

@Component
@RequestScope
public class SecurityContext {

    @Getter
    @Setter
    private User identified;
}
