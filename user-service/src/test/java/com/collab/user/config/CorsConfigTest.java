package com.collab.user.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CorsConfigTest {

    @Test
    void corsConfigurerBeanPresent(ApplicationContext context) {
        WebMvcConfigurer bean = context.getBean("corsConfigurer", WebMvcConfigurer.class);
        assertThat(bean).isNotNull();
    }
}
