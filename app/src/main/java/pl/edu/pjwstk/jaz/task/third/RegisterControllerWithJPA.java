package pl.edu.pjwstk.jaz.task.third;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.third.user.UserDTO;
import pl.edu.pjwstk.jaz.task.third.user.UserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
public class RegisterControllerWithJPA {
    private final UserService userService;

    public RegisterControllerWithJPA(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("third/register")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {
        if (user.isEmpty()) {
            return new ResponseEntity<>("You must provide a user name and password.", HttpStatus.BAD_REQUEST);
        } else if (user.usernameIsEmpty()) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.BAD_REQUEST);
        } else if (user.passwordIsEmpty()) {
            return new ResponseEntity<>("You must provide a password.", HttpStatus.BAD_REQUEST);
        } else if (user.idRoleIsEmpty()) {
            return new ResponseEntity<>("You must provide a role.", HttpStatus.BAD_REQUEST);
        }
        try {
            userService.saveUser(user);
        } catch (EntityExistsException | EntityNotFoundException message) {
            return new ResponseEntity<>(message.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Registered.", HttpStatus.CREATED);
    }
}
