package pl.edu.pjwstk.jaz.task.third;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.task.third.user.UserService;


@RestController
public class GetUserController {
    private final UserService userService;

    public GetUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("third/getUser/{username}")
    public ResponseEntity<String> findByUsername(@PathVariable("username") String username) {
        var user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user.get().toString(), HttpStatus.OK);
    }

}
