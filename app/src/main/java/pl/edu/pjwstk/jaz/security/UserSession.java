package pl.edu.pjwstk.jaz.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.jaz.entity.UserEntity;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class UserSession {
    private boolean isLogged = false;
    private UserEntity userEntity;

    public boolean isLoggedIn() {
        return isLogged;
    }

    public void setLoggedIn(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public void logIn() {
        isLogged = true;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
