package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.Category;
import com.vitor.pizzaria.model.entity.Product;
import com.vitor.pizzaria.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> getProductsByRestaurant(Restaurant restaurant);

    List<Product> getProductsByCategory(Category category);

    List<Product> getProductsByRestaurantIdAndActiveTrue(UUID tenant);
}
