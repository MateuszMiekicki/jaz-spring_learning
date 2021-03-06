package pl.edu.pjwstk.jaz.task.second.filters;

import org.springframework.http.HttpStatus;
import pl.edu.pjwstk.jaz.task.second.component.UserSession;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends HttpFilter {
    private final UserSession userSession;

    public AuthenticationFilter(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isUserLogged()) {
            super.doFilter(request, response, chain);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean isUserLogged() {
        return userSession.isLoggedIn();
    }
}
