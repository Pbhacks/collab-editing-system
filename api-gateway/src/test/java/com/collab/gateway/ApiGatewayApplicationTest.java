package com.collab.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "server.port=0",  // Use random port for testing
    "management.server.port=0"
})
class ApiGatewayApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring Boot application context loads successfully
        // It will fail if there are any configuration errors or missing dependencies
    }

    @Test
    void applicationStartsSuccessfully() {
        // If we reach this point, the application has started successfully
        // This test ensures all beans are created and wired correctly
        assert true;
    }
}