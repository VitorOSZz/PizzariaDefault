package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.enums.PizzaSize;
import com.vitor.pizzaria.model.entity.PizzaModel;
import com.vitor.pizzaria.model.entity.PizzaPriceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PizzaPriceRepository extends JpaRepository<PizzaPriceModel, Long> {

    @Query("SELECT pizza FROM PizzaPriceModel pizza WHERE pizza.size = :size")
    List<PizzaPriceModel> getPizzaPriceModelBySize(@Param("size") PizzaSize size);

    @Query("SELECT pizza FROM PizzaPriceModel pizza WHERE pizza.pizza_id = :pizzaId and pizza.size = :pizzaSize")
    PizzaPriceModel getPizzaPriceModelBySizeAndId(@Param("pizzaId") Long pizzaId, @Param("pizzaSize") PizzaSize pizzaSize);
}
