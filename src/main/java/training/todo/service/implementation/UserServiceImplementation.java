package training.todo.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import training.todo.UserRepository;
import training.todo.exceptions.UserServiceException;
import training.todo.io.entity.UserEntity;
import training.todo.service.UserService;
import training.todo.shared.Utils;
import training.todo.shared.dto.UserDto;
import training.todo.ui.model.response.ErrorMessages;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        // checking whether the user with the given username and email already exists or not...
        if(userRepository.findByEmail(user.getEmail()) != null
                || userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        UserEntity userEntity = new UserEntity();
        // the fields in user entity class and user dto class must match!
        BeanUtils.copyProperties(user, userEntity);

        // removing the leading and trailing whitespaces...
        userEntity.setFirstName(userEntity.getFirstName().trim());
        userEntity.setLastName(userEntity.getLastName().trim());
        userEntity.setUsername(userEntity.getUsername().trim());

        // generating a random public id and encrypting the password...
        String publicUserId = utils.generateUserId(30);
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);

        // storing the newly created user in the database...
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUserByUserId(String userId, UserDto user) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            return null;
        }
        userEntity.setFirstName(user.getFirstName().trim());
        userEntity.setLastName(user.getLastName().trim());
        UserEntity updatedUser = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUser, userDto);
        return user;
    }

    @Override
    public void deleteUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getAllUsers(int page, int limit) {
        List<UserDto> users = new ArrayList<UserDto>();
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> listOfUsers = usersPage.getContent();

        for(UserEntity user : listOfUsers) {
            UserDto newUser = new UserDto();
            BeanUtils.copyProperties(user, newUser);
            users.add(newUser);
        }

        return users;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto user = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(userEntity, user);
        return user;
    }
}
