package pl.edu.pjwstk.jaz.task.second.component.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.second.component.UserDTO;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

@RestController
public class RegisterController {
    final UsersRepository usersRepository;

    @Autowired
    public RegisterController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        try {
            usersRepository.insert(user);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        } catch (UserExistException | BadCredentialsException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
        }
    }
}
