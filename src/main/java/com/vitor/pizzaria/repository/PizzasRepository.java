package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.model.entity.PizzaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PizzasRepository extends JpaRepository<PizzaModel, Long> {

    @Query("SELECT row FROM PizzaModel row")
    List<PizzaModel> getAllPizzas();
}
