package com.collab.gateway.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.cors.reactive.CorsWebFilter;

@SpringBootTest
class CorsConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CorsWebFilter corsWebFilter;

    @Test
    void corsWebFilterIsNotNull() {
        assertThat(corsWebFilter).isNotNull();
    }

    @Test
    void corsConfigurationBeanExists() {
        // Test that the CorsWebFilter bean is properly configured
        assertThat(corsWebFilter).isInstanceOf(CorsWebFilter.class);
    }

    @Test
    void corsConfigBeanIsRegistered() {
        // Verify that the CorsConfig bean is registered in the application context
        assertThat(applicationContext.getBean(CorsConfig.class)).isNotNull();
    }

    @Test
    void corsWebFilterBeanIsRegistered() {
        // Verify that the CorsWebFilter bean is available
        assertThat(applicationContext.getBean("corsWebFilter", CorsWebFilter.class)).isNotNull();
    }
}