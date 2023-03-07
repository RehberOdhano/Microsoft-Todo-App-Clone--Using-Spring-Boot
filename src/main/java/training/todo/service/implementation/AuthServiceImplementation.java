package training.todo.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import training.todo.UserRepository;
import training.todo.exceptions.UserServiceException;
import training.todo.io.entity.Role;
import training.todo.io.entity.UserEntity;
import training.todo.security.JwtService;
import training.todo.service.AuthenticationService;
import training.todo.shared.Utils;
import training.todo.ui.model.request.LoginRequest;
import training.todo.ui.model.request.RegisterRequest;
import training.todo.ui.model.response.AuthenticationResponse;
import training.todo.ui.model.response.ErrorMessages;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest user) {
        if(userRepository.findByEmail(user.getEmail()) != null
                || userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        // removing the leading and trailing whitespaces and assigning the values to the
        // attributes of the new user...
        userEntity.setFirstName(userEntity.getFirstName().trim());
        userEntity.setLastName(userEntity.getLastName().trim());
        userEntity.setUsername(userEntity.getUsername().trim());
        userEntity.setEmail(userEntity.getEmail());
        userEntity.setRole(Role.USER);

        // generating a random public userId and encrypting the password...
        String publicUserId = utils.generateUserId(30);
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);

        // storing the newly created user in the database...
        var storedUserDetails = userRepository.save(userEntity);

        // generating the token based on the details of the newly created user...
        var jwtToken = jwtService.generateJwtToken(storedUserDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail());
        if(user == null) {
            throw new UserServiceException(ErrorMessages.INVALID_CREDENTIALS.getErrorMessage());
        }
        if(!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserServiceException(ErrorMessages.INVALID_CREDENTIALS.getErrorMessage());
        }

        // generating the token based on the details of the fetched user...
        var jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }
}
