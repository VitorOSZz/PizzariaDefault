package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> getCategoriesByRestaurantId(UUID restaurantId);

    List<Category> getCategoriesByRestaurantIdAndActiveTrue(UUID restaurantId);

    Category getCategoryById(UUID id);
}
