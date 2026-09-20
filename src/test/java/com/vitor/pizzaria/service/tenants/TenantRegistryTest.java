package com.vitor.pizzaria.service.tenants;

import com.vitor.pizzaria.repository.RestaurantRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
class TenantRegistryTest {

    @Autowired
    TenantRegistry tenantRegistry;

    @DisplayName("getRestaurantId - Getting an invalid specific tenant")
    @Test
    void getRestaurantIdInvalid() {
        assertNull(
                tenantRegistry.getRestaurantId("test")
        );
    }

    @DisplayName("getRestaurantId - Getting a valid specific tenant")
    @Test
    void getRestaurantIdValid() {
        String slug = "pizzaria-default";
        String uuid = "f647dd6d-3cef-4d3c-824c-10d49fcc6546";

        // getting UUID from a specific and valid slug
        UUID tryRestaurantId = tenantRegistry.getRestaurantId(slug);

        // creating the correct UUID to compare
        UUID restaurantCorrect = UUID.fromString(uuid);

        assertEquals(tryRestaurantId, restaurantCorrect);
    }
}