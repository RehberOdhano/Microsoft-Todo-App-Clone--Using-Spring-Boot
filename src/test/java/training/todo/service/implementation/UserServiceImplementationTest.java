package training.todo.service.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import training.todo.UserRepository;
import training.todo.exceptions.UserServiceException;
import training.todo.io.entity.Role;
import training.todo.io.entity.UserEntity;
import training.todo.shared.Utils;
import training.todo.shared.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    /*
    * @Mock creates a mock, and @InjectMocks creates an instance of the class and injects
    * the mocks that are created with the @Mock annotations into this instance.
    * */
    @InjectMocks
    UserServiceImplementation userService; // this is the class under test
    @Mock // this annotation is shorthand for the Mockito.Mock() - mocks are used as a replacement for a
            // dependency
    UserRepository userRepository;
    @Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserEntity user;

    private String rawPassword = "aq745wsd69102sd";
    private String userId = "78sd52abc99";
    private String email = "hello@gmail.com";
    private String username = "hello_world";
    private String firstName = "hello";
    private String lastName = "world";

    @BeforeEach
    void setUp() {
        // The MockitoAnnotations.openMocks(this) call tells Mockito to scan this test class
        // instance for any fields annotated with the @Mock annotation and initialize those
        // fields as mocks, and create an instance of the field annotated with @InjectMocks and
        // try to inject the mocks into it...
        MockitoAnnotations.openMocks(this);

        user = new UserEntity(); // stub
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(rawPassword);
        user.setUserId(userId);
        user.setRole(Role.USER);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void getUserByUserId() {

        // when the method findByUserId is called on the userRepository, accepting any input string,
        // it should then return a specific object... in this case user...
        // we want to configure the mock and define what to do when specific methods of the mock are called.
        // This is called stubbing.
        when(userRepository.findByUserId(anyString())).thenReturn(user);

        UserDto userDto = userService.getUserByUserId(userId);
        assertNotNull(userDto);
        assertEquals(user.getFirstName(), userDto.getFirstName());
    }
    @Test
    void getUserByUserId_UserServiceException() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        assertThrows(UserServiceException.class, () -> {
            userService.getUserByUserId(userId);
        });
    }
    @Test
    void getUserByUsername_UserServiceException() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        assertThrows(UserServiceException.class, () -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userService.createUser(userDto);
        });
    }

    @Test
    void getUserByEmail_UserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        assertThrows(UserServiceException.class, () -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userService.createUser(userDto);
        });
    }


    @Test
    void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(rawPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        UserDto storedUser = userService.createUser(userDto);

        assertNotNull(storedUser);
        assertEquals(user.getFirstName(), storedUser.getFirstName());
        assertEquals(user.getLastName(), storedUser.getLastName());
        assertEquals(user.getEmail(), storedUser.getEmail());

        verify(utils, times(1)).generateUserId(30);
        verify(bCryptPasswordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void updateUserByUserId() {
    }

    @Test
    void deleteUserByUserId() {
    }

    @Test
    void getAllUsers() {
    }
}