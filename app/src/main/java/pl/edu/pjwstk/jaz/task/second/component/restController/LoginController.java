package pl.edu.pjwstk.jaz.task.second.component.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import java.util.Map;

@RestController
public class LoginController {
    final UsersRepository usersRepository;

    @Autowired
    public LoginController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> payload) {
        if (payload.isEmpty()) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.BAD_REQUEST);
        }
        try {
            var user = usersRepository.getUser(payload.get("username").toString());
            if (user.getPassword().equals(payload.get("password").toString())) {
                return new ResponseEntity<>("Logged in", HttpStatus.OK);
            }
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
