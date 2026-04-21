import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.interview_tracking_system.backend.entity.*;
import com.interview_tracking_system.backend.enums.Role;

class UserTest {
    
    /**
     * Test for User entity.
     */
    @Test
    void testUser() {
        User user = new User();

        user.setName("Raj");
        user.setEmail("raj@test.com");
        user.setPassword("12345");
        user.setRole(Role.HR);
        user.setActive(true);

        assertEquals("Raj", user.getName());
        assertEquals("raj@test.com", user.getEmail());
        assertEquals(Role.HR, user.getRole());
        assertTrue(user.isActive());
    }
}