package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.PizzaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PizzasRepository extends JpaRepository<PizzaModel, Long> {

    @Query("SELECT row FROM PizzaModel row")
    List<PizzaModel> getAllPizzas();

    @Query("SELECT pizza FROM PizzaModel pizza WHERE pizza.pizza_id = :pizza_id")
    PizzaModel getPizzaModelByPizza_id(@Param("pizza_id") Long pizza_id);
}
