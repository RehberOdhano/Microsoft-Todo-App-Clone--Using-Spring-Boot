package training.todo.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @InjectMocks
    Utils utils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(30);
        String newUserId = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(newUserId);
        assertTrue(userId.length() == 30);
        assertTrue(newUserId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(newUserId));
    }
}