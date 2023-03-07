package training.todo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import training.todo.ui.model.request.LoginRequest;
import training.todo.ui.model.request.RegisterRequest;
import training.todo.ui.model.response.AuthenticationResponse;

public interface AuthenticationService extends UserDetailsService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
}
