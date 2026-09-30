package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
    List<ProductPrice> getProductPricesByRestaurantId(UUID tenant);

    List<ProductPrice> getProductPricesByRestaurantIdAndActiveTrue(UUID tenant);
}
