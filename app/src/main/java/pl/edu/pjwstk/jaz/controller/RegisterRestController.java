package pl.edu.pjwstk.jaz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.DTO.UserDTO;
import pl.edu.pjwstk.jaz.entity.RoleEntity;
import pl.edu.pjwstk.jaz.entity.UserEntity;
import pl.edu.pjwstk.jaz.repository.UserRepository;

@RestController
public class RegisterRestController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("allezon/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password is empty.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Such an email exists in the database.", HttpStatus.CONFLICT);
        }
        userRepository.save(new UserEntity()
                .withEmail(user.getEmail())
                .withPassword(passwordEncoder.encode(user.getPassword()))
                .withRole(new RoleEntity().withId(2L)));
        return new ResponseEntity<>("Registered.", HttpStatus.CREATED);
    }
}