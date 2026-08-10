package com.vitor.pizzaria.service;

import com.vitor.pizzaria.model.entity.PizzaModel;
import com.vitor.pizzaria.repository.PizzasRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PizzaService {

    private final PizzasRepository pizzaRepository;

    public PizzaService(PizzasRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<PizzaModel> getPizzas() {
        return pizzaRepository.getAllPizzas();
    }
}
