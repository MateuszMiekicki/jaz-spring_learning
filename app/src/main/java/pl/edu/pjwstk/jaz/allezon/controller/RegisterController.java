package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.allezon.DTO.UserDTO;
import pl.edu.pjwstk.jaz.allezon.service.RegisterService;

@RestController
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("allezon/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password is empty.", HttpStatus.BAD_REQUEST);
        }
        if (registerService.emailExists(user.getEmail())) {
            return new ResponseEntity<>("Such an email exists in the database.", HttpStatus.CONFLICT);
        }
        registerService.saveUser(user);
        return new ResponseEntity<>("Registered.", HttpStatus.CREATED);
    }
}
