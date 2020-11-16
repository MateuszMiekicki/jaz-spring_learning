package pl.edu.pjwstk.jaz.task.second.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;

import java.util.List;
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

    @GetMapping("user")
    public ResponseEntity<String> user(@RequestParam(value = "username") List<String> username) {
        if (username.isEmpty() || username == null) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.OK);
        }
        try {
            String string = "";
            for (int i = 0; i < username.size(); ++i) {
                string += usersRepository.getUser(username.get(i)).toString();
                if (i == (username.size() - 1)) {
                    continue;
                }
                if (username.size() != 1) {
                    string += ",\n";
                }
            }
            return new ResponseEntity<>(string, HttpStatus.OK);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
    }
}
