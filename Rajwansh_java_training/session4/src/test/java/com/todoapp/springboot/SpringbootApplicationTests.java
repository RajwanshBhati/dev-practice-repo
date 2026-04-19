package com.todoapp.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    void mainMethod_shouldRunWithoutException() {
        assertDoesNotThrow(() -> {
            SpringbootApplication.main(new String[]{});
        });
    }

}
