package training.todo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import training.todo.shared.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUserByUserId(String userId, UserDto userDto);
    void deleteUserByUserId(String userId);
    List<UserDto> getAllUsers(int page, int limit);
    UserDto getUserByUserId(String userId);
}
