package pl.edu.pjwstk.jaz.allezon.service;

import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.DTO.UserDTO;

@Service
public class UserService {
    private final RegisterService registerService;

    public UserService(RegisterService registerService) {
        this.registerService = registerService;
    }

    public void saveUser(UserDTO user) {
        registerService.saveUser(user);
    }

    public boolean emailExists(String email) {
        return registerService.emailExists(email);
    }
}
