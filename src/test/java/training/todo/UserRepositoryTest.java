package training.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import training.todo.io.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    UserEntity user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUsername("hello world");
        user.setEmail("hello@gmail.com");
        user.setUserId("4sd785awds96");
    }

    @Test
    void findByEmail() {
        UserEntity fetchedUser = userRepository.findByEmail(user.getEmail());
        assertNull(fetchedUser);

        user.setEmail("test@gmail.com");
        fetchedUser = userRepository.findByEmail(user.getEmail());
        assertNotNull(fetchedUser);
    }

    @Test
    void findByUserId() {
    }

    @Test
    void findByUsername() {
    }
}