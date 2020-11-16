package pl.edu.pjwstk.jaz.task.second.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;

import java.util.Map;

@RestController
public class AccountController {
    final UsersRepository usersRepository;

    @Autowired
    public AccountController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping(value = "register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> payload) {
        UserDTO user = new UserDTO();
        user.setUsername(payload.get("username").toString());
        user.setPassword(payload.get("password").toString());
        user.setRole(payload.get("role").toString());
        try {
            usersRepository.insert(user);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        } catch (UserExistException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }
}
