package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Restaurant getRestaurantById(UUID tenant);

    // Get a product by category and size
}
