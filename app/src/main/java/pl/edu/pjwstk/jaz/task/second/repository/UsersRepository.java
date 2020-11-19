package pl.edu.pjwstk.jaz.task.second.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.task.second.component.UserDTO;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@Scope("singleton")
public class UsersRepository {
    Map<String, UserDTO> users = new LinkedHashMap<>();

    public void insert(UserDTO user) {
        if (users.containsKey(user.getUsername())) {
            throw new UserExistException("A user with this name already exists: " + user.getUsername());
        }
        if ((user.getUsername() == null || user.getUsername().isEmpty()) || (user.getPassword() == null || user.getPassword().isEmpty())) {
            throw new BadCredentialsException("Incorrect data. Password or login is empty.");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("");
        }
        users.put(user.getUsername(), user);
    }

    public UserDTO getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        throw new UserNotFoundException("There is no user with this name: " + username);
    }
}
