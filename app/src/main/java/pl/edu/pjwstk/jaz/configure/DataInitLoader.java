package pl.edu.pjwstk.jaz.configure;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pjwstk.jaz.entity.RoleEntity;
import pl.edu.pjwstk.jaz.entity.UserEntity;
import pl.edu.pjwstk.jaz.repository.UserRepository;

@Configuration
public class DataInitLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {
        String email = "admin@jaz.com";
        String password = "admin";
        if (userRepository.findByEmail(email) != null) {
            return;
        }
        userRepository.save(new UserEntity()
                .withEmail(email)
                .withPassword(passwordEncoder.encode(password))
                .withRole(new RoleEntity().withId(1L)));
    }
}