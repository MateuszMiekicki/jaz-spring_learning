package pl.edu.pjwstk.jaz.task.second.manualSpringSecurity.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.pjwstk.jaz.task.second.manualSpringSecurity.filters.AuthenticationFilter;
import pl.edu.pjwstk.jaz.task.second.component.UserSession;
import pl.edu.pjwstk.jaz.task.second.manualSpringSecurity.filters.AuthorizationFilter;

@Configuration
public class WebSecurityConfiguration {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(UserSession userSession){
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(userSession));
        registrationBean.addUrlPatterns("/forAuthenticated");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizedFilter(UserSession userSession){
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationFilter(userSession));
        registrationBean.addUrlPatterns("/forAuthorized");
        registrationBean.addUrlPatterns("/userList");
        return registrationBean;
    }
}
