package pl.edu.pjwstk.jaz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.DTO.UserDTO;
import pl.edu.pjwstk.jaz.entity.UserEntity;
import pl.edu.pjwstk.jaz.repository.UserRepository;
import pl.edu.pjwstk.jaz.security.AuthenticationToken;
import pl.edu.pjwstk.jaz.security.UserSession;

import javax.persistence.EntityManager;

@RestController
public class LoginRestController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private final EntityManager entityManager;

    public LoginRestController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
        this.entityManager = entityManager;
    }

    @PostMapping("allezon/login")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password is empty.", HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null) {
            return new ResponseEntity<>("Such an email not exists in the database.", HttpStatus.BAD_REQUEST);
        }
        if (passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
            userSession.logIn();
            SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(userEntity, entityManager));
            return new ResponseEntity<>("Logged in.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Email or password is incorrect.", HttpStatus.UNAUTHORIZED);
    }
}
