package training.todo.exceptions;

public class UserServiceException extends RuntimeException {

    public static final long serialVersionUID = -2516247559045659108L;
    public UserServiceException(String message) {
        super(message);
    }
}
