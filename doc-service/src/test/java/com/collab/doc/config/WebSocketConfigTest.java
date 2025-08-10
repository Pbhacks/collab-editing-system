package com.collab.doc.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = WebSocketConfig.class)
class WebSocketConfigTest {

    @Test
    void webSocketConfigurer_shouldBeInContext(ApplicationContext context) {
        assertNotNull(context.getBean(WebSocketMessageBrokerConfigurer.class));
    }

    @Test
    void registerStompEndpoints_shouldNotThrowException() {
    WebSocketConfig config = new WebSocketConfig();

    // Mock the registry and the fluent registration to avoid NPEs on chained calls
    StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
    StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);

    when(registry.addEndpoint("/ws")).thenReturn(registration);
    when(registration.setAllowedOriginPatterns(any())).thenReturn(registration);
    // withSockJS returns a SockJsServiceRegistration; return a dummy mock to complete the chain
    when(registration.withSockJS()).thenReturn(mock(SockJsServiceRegistration.class));

    // Should not throw
    config.registerStompEndpoints(registry);
    }
}
