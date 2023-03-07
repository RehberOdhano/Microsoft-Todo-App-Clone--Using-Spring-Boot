package training.todo.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.assertj.core.api.Assert;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import training.todo.UserRepository;
import training.todo.io.entity.Role;
import training.todo.io.entity.UserEntity;
import training.todo.service.implementation.UserServiceImplementation;
import training.todo.shared.dto.UserDto;
import training.todo.ui.model.request.UserDetailsRequestModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    UserServiceImplementation userService;
    @Mock
    UserRepository userRepository;

    /*@Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;*/

    private UserDto userDto;
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    private String rawPassword = "aq745wsd69102sd";
    private String userId = "78sd52abc99";
    private String email = "hello@gmail.com";
    private String username = "hello_world";
    private String firstName = "hello";
    private String lastName = "world";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDto = new UserDto(); // stub
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setPassword(rawPassword);
        userDto.setUserId(userId);
        userDto.setRole(Role.USER);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUserById() throws Exception {
        /*when(userService.getUserByUserId(anyString())).thenReturn(user);
        UserRest fetchedUser = userController.getUser(user.getUserId());

        assertEquals(user.getFirstName(), fetchedUser.getFirstName());
        assertNotNull(fetchedUser);
        assertEquals(user.getLastName(), fetchedUser.getLastName());
        assertEquals(user.getEmail(), fetchedUser.getEmail());*/

        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders
                .get("/api/get/user/78sd52abc99")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers(anyInt(), 10))
                .thenReturn(new ArrayList<UserDto>(Arrays.asList(userDto)));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders
                .get("/api/users")
                .requestAttr("page", 0)
                .requestAttr("limit", 10)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        ResultActions result = mockMvc.perform(getRequest);
        int numOfUsers = new JSONArray(result.andReturn().getResponse().getContentAsString()).length();

        assertTrue(result.andReturn().getResponse().getStatus() == 200);
        assertTrue(numOfUsers >= 0 && numOfUsers <= 10);
    }

    @Test
    void createUser() throws Exception {
        var user = UserEntity.builder()
                .userId(userId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(rawPassword)
                .role(Role.USER)
                .build();

        when(userRepository.save(user)).thenReturn(user);

        var userDetails = new UserDetailsRequestModel();
        BeanUtils.copyProperties(user, userDetails);

        // Building a post request with user payload...
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders
                .post("/api/add/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(this.objectWriter.writeValueAsString(userDetails));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}