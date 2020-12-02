package pl.edu.pjwstk.jaz.task.second.component.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.second.component.UserSession;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LoginController {
    final UsersRepository usersRepository;
    final UserSession userSession;

    @Autowired
    public LoginController(UsersRepository usersRepository, UserSession userSession) {
        this.usersRepository = usersRepository;
        this.userSession = userSession;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        if (payload.isEmpty()) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.BAD_REQUEST);
        }
        try {
            var user = usersRepository.getUser(payload.get("username").toString());
            if (user.getPassword().equals(payload.get("password").toString())) {
                userSession.setLoggedIn(true);
                userSession.setSessionID(request.getRequestedSessionId());
                userSession.setRole(user.getRole());
                return new ResponseEntity<>("Logged in", HttpStatus.OK);
            }
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
