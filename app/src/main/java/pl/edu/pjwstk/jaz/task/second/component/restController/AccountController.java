package pl.edu.pjwstk.jaz.task.second.component.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import java.util.Map;

@RestController
public class AccountController {
    final UsersRepository usersRepository;

    @Autowired
    public AccountController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /* @PostMapping(value = "delete")
     public ResponseEntity<String> delete(@RequestBody Map<String, Object> payload) {
         if (payload.isEmpty() || payload == null) {
             return new ResponseEntity<>("You must provide a user name.", HttpStatus.BAD_REQUEST);
         }
         try {
             usersRepository.delete(payload.get("username").toString());
             return new ResponseEntity<>("Deleted.", HttpStatus.OK);
         } catch (UserNotFoundException exception) {
             return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
         }
     }
     */

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> payload) {
        if (payload.isEmpty() || payload == null) {
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
