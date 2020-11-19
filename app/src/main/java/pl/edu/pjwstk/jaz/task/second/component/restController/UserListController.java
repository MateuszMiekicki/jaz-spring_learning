package pl.edu.pjwstk.jaz.task.second.component.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import java.util.List;

@RestController
public class UserListController {
    final UsersRepository usersRepository;

    @Autowired
    public UserListController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("userList")
    public ResponseEntity<String> user(@RequestParam(value = "username") List<String> username) {
        if (username.isEmpty()) {
            return new ResponseEntity<>("You must provide a user name.", HttpStatus.OK);
        }
        try {
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < username.size(); ++i) {
                string.append(usersRepository.getUser(username.get(i)).toString());
                if (i == (username.size() - 1)) {
                    continue;
                }
                if (username.size() != 1) {
                    string.append(",\n");
                }
            }
            return new ResponseEntity<>(string.toString(), HttpStatus.OK);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
    }
}
