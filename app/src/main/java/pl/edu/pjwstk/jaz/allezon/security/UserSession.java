package pl.edu.pjwstk.jaz.allezon.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class UserSession {
    private boolean isLogged = false;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return isLogged;
    }

    public void setLoggedIn(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public void logIn() {
        isLogged = true;
    }
}
