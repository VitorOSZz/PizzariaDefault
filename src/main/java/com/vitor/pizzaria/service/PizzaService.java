package com.vitor.pizzaria.service;

import com.vitor.pizzaria.enums.PizzaSize;
import com.vitor.pizzaria.model.dto.PizzaDTO;
import com.vitor.pizzaria.model.entity.PizzaModel;
import com.vitor.pizzaria.model.entity.PizzaPriceModel;
import com.vitor.pizzaria.repository.PizzaPriceRepository;
import com.vitor.pizzaria.repository.PizzasRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaService {

    private final PizzasRepository pizzaRepository;
    private final PizzaPriceRepository pizzaPriceRepository;


    public PizzaService(PizzasRepository pizzaRepository, PizzaPriceRepository pizzaPriceRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPriceRepository = pizzaPriceRepository;
    }

    public List<PizzaModel> getPizzas() {
        return pizzaRepository.getAllPizzas();
    }

    private PizzaSize convertSize(String option) {
        return switch (option) {
            case "medium" -> PizzaSize.MEDIUM;
            case "big" -> PizzaSize.BIG;
            case "giant" -> PizzaSize.GIANT;
            case "napoletana" -> PizzaSize.NAPOLETANA;
            default -> throw new IllegalArgumentException("Invalid size option");
        };
    }

    public List<PizzaDTO> getPizzasBySize(String size) {
        List<PizzaDTO> pizzas = new ArrayList<>();

        List<PizzaPriceModel> pizzasPrice = pizzaPriceRepository.getPizzaPriceModelBySize(convertSize(size));

        for (PizzaPriceModel pizzaPrice : pizzasPrice) {
            PizzaModel pizzaModel = pizzaRepository.getPizzaModelByPizza_id(pizzaPrice.getPizza_id());
            Long id = pizzaPrice.getPizza_id();
            String name = pizzaModel.getName();
            String description = pizzaModel.getDescription();
            String imageName = pizzaModel.getImage_name();
            String imageFit = pizzaModel.getImage_fit();
            PizzaSize pizzaSize = pizzaPrice.getSize();
            BigDecimal price = pizzaPrice.getPrice();

            pizzas.add(new PizzaDTO(id, name, description, imageName, imageFit, pizzaSize, price));
        }

        return pizzas;
    }

    public PizzaDTO getPizzaBySizeAndId(String size, Long id) {
        PizzaPriceModel pizzaPriceModel = pizzaPriceRepository.getPizzaPriceModelBySizeAndId(id, convertSize(size));
        PizzaModel pizzaModel = pizzaRepository.getPizzaModelByPizza_id(pizzaPriceModel.getPizza_id());

        return new PizzaDTO(
                pizzaPriceModel.getPizza_id(),
                pizzaModel.getName(),
                pizzaModel.getDescription(),
                pizzaModel.getImage_name(),
                pizzaModel.getImage_fit(),
                pizzaPriceModel.getSize(),
                pizzaPriceModel.getPrice()
        );
    }
}
