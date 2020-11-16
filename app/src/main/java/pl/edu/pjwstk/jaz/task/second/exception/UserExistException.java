package pl.edu.pjwstk.jaz.task.second.exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
