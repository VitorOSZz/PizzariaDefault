package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface SizeRepository extends JpaRepository<Size, UUID> {
    List<Size> getSizesByRestaurantId(UUID restaurantId);

    List<Size> getSizesByRestaurantIdAndCategoryIdAndActiveTrue(UUID restaurantId, UUID categoryId);

    List<Size> getSizesByRestaurantIdAndActiveTrue(UUID tenant);
}
