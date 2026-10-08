package com.vitor.pizzaria.service.tenants;

import com.vitor.pizzaria.model.entity.Restaurant;
import com.vitor.pizzaria.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TenantRegistryTest {

    @Autowired
    TenantRegistry tenantRegistry;

    @Autowired
    RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(UUID.fromString("f647dd6d-3cef-4d3c-824c-10d49fcc6546"));
        restaurant.setStatus("ACTIVE");
        restaurant.setName("Pizzaria Default");
        restaurant.setSlug("pizzaria-default");
        restaurant.setCreatedAt(OffsetDateTime.now());
        restaurant.setUpdatedAt(OffsetDateTime.now());
        restaurantRepository.save(restaurant);

        tenantRegistry.loadTenants();
    }

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

        assertEquals(restaurantCorrect, tryRestaurantId);
    }
}
