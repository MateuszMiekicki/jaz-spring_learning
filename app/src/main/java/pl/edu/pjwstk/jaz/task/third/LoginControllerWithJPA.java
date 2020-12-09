package pl.edu.pjwstk.jaz.task.third;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.second.component.UserSession;
import pl.edu.pjwstk.jaz.task.third.user.UserDTO;
import pl.edu.pjwstk.jaz.task.third.user.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginControllerWithJPA {
    private final UserService userService;
    final UserSession userSession;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginControllerWithJPA(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @PostMapping("third/login")
    public ResponseEntity<String> login(@RequestBody UserDTO user, HttpServletRequest request) {
        if (user.isEmpty()) {
            return new ResponseEntity<>("You must provide a user name and password.", HttpStatus.BAD_REQUEST);
        } else if (user.usernameIsEmpty()) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.BAD_REQUEST);
        } else if (user.passwordIsEmpty()) {
            return new ResponseEntity<>("You must provide a password.", HttpStatus.BAD_REQUEST);
        }
        var findUser = userService.findByUsername(user.getUsername());
        if (findUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), findUser.get().getPassword())) {
                userSession.setLoggedIn(true);
            } else {
                return new ResponseEntity<>("Wrong password.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("The user does not exist.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Login.", HttpStatus.ACCEPTED);
    }
}
