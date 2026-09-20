package com.vitor.pizzaria.service.tenants;

import com.vitor.pizzaria.model.entity.Restaurant;
import com.vitor.pizzaria.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TenantRegistry {

    private final RestaurantRepository restaurantRepository;
    private final Map<String, UUID> tenants = new HashMap<>();

    public TenantRegistry(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostConstruct
    public void loadTenants() {

        List<Restaurant> restaurants = restaurantRepository.findAll();

        for (Restaurant restaurant : restaurants) {
            tenants.put(restaurant.getSlug(), restaurant.getId());
        }

        System.out.println("PostConstruct tenants: " + tenants);
    }

    public UUID getRestaurantId(String slug) {
        return tenants.get(slug);
    }

}
