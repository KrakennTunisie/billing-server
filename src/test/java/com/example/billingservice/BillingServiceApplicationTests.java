package com.example.billingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestPropertySource(properties = {
    "spring.config.import=optional:configserver:",
    "spring.cloud.config.enabled=false"
})
class BillingServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
