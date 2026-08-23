package com.vitor.pizzaria.repository;

import com.vitor.pizzaria.enums.DrinkCategory;
import com.vitor.pizzaria.model.entity.DrinkModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrinkRepository extends JpaRepository<DrinkModel, Long> {

    @Query("SELECT row FROM DrinkModel row")
    List<DrinkModel> getDrinks();

    @Query("SELECT drink FROM DrinkModel drink WHERE drink.category = :category")
    List<DrinkModel>  getDrinksByCategory(@Param("category") DrinkCategory category);

    @Query("SELECT drink FROM DrinkModel drink WHERE drink.id = :id")
    List<DrinkModel> getDrinkById(@Param("id") Long id);
}
