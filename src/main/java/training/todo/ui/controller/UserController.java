package training.todo.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import training.todo.service.UserService;
import training.todo.shared.dto.UserDto;
import training.todo.ui.model.request.UserDetailsRequestModel;
import training.todo.ui.model.response.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;

    //@GetMapping(path = "/get/user/{userId}") // by default response will be sent in the json format...
    @GetMapping(path = "/get/user/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    ) // to send response as a json & xml
    public UserRest getUser(@PathVariable String userId) {
        UserRest user = new UserRest();
        System.out.println("id: " + userId);
        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto, user);
        System.out.println("user: " + user);
        return user;
    }

    @GetMapping(path = "/users",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public List<UserRest> getAllUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                      @RequestParam(value="limit", defaultValue = "10") int limit) {

        // fetching the users from the database based on the page number and limit... = pagination
        List<UserDto> fetchedUsers = userService.getAllUsers(page, limit);
        List<UserRest> users = new ArrayList<UserRest>();

        for(UserDto user : fetchedUsers) {
            UserRest newUser = new UserRest();
            BeanUtils.copyProperties(user, newUser);
            users.add(newUser);
        }

        return users;
    }

    @PostMapping(path = "/add/user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    // UserDetailsRequestModel class is used to create a java object out of the request body...
    // this class will be used by the framework which will take the request body and convert the json
    // payload document into java object (POJO) of type UserDetailsRequestModel class and vice versa...
    // this class should match the attributes that are present in the request body...

    // class will be used by the framework to create a json payload  which will be sent back as a response...
    // this class contains only those attributes which we want to return in the form of json payload
    // as a response...
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if(userDetails.getFirstName().isEmpty() || userDetails.getFirstName().isBlank()) {
            throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        UserRest returnUser = new UserRest(); // will be sent as a response

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnUser);

        return returnUser;
    }

    @PutMapping(path = "/update/user/{userId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails)
            throws Exception {
        if(userDetails.getFirstName().isEmpty() || userDetails.getFirstName().isBlank()) {
            throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserRest returnUser = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUserByUserId(userId, userDto);
        if(updateUser == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(updateUser, returnUser);
        return returnUser;
    }

    @DeleteMapping(path = "/delete/user/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel deleteUser(@PathVariable String userId) {
        OperationStatusModel response = new OperationStatusModel();
        response.setOperationName(RequestOperations.DELETE.name());
        userService.deleteUserByUserId(userId);
        response.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return response;
    }
}
