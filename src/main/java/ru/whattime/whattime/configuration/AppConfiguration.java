package ru.whattime.whattime.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.whattime.whattime.filter.IdentificationFilter;

@Configuration
@EnableWebMvc
@AllArgsConstructor
public class AppConfiguration {

    private IdentificationFilter identificationFilter;

    @Bean
    public FilterRegistrationBean<IdentificationFilter> myFilterRegistration() {
        FilterRegistrationBean<IdentificationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(identificationFilter);
        filterRegistrationBean.addUrlPatterns("/api/v1/login/test");
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return filterRegistrationBean;
    }
}
