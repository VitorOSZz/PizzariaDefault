package com.vitor.pizzaria.service;

import com.vitor.pizzaria.enums.DrinkCategory;
import com.vitor.pizzaria.enums.PizzaSize;
import com.vitor.pizzaria.model.dto.CreateProductRequestDTO;
import com.vitor.pizzaria.model.dto.DrinkDTO;
import com.vitor.pizzaria.model.entity.DrinkModel;
import com.vitor.pizzaria.repository.DrinkRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
class DrinkService {

    final DrinkRepository drinkRepository;

    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public DrinkCategory convertStringCategoryToDrinkCategory(String stringCategory) {
        return switch (stringCategory) {
            case "soda", "sodas" ->  DrinkCategory.SODA;
            case "water", "waters" ->  DrinkCategory.WATER;
            case "beer", "beers" ->  DrinkCategory.BEER;
            default -> throw new IllegalStateException("Invalid drink category: " + stringCategory);
        };
    }

    public List<DrinkDTO> getDrinksByCategory(String stringCategory) {
        List<DrinkDTO> list = new ArrayList<>();

        DrinkCategory category =  convertStringCategoryToDrinkCategory(stringCategory);
        for (DrinkModel drinkModel : drinkRepository.getDrinksByCategory(category)) {
            list.add(new DrinkDTO(
                    drinkModel.getId(),
                    drinkModel.getName(),
                    drinkModel.getDescription(),
                    drinkModel.getImage(),
                    drinkModel.getImage_fit(),
                    category,
                    drinkModel.getSize(),
                    drinkModel.getPrice()

            ));
        }

        return list;
    }

    public DrinkDTO getDrinkById(Long id) {
        DrinkModel drinkModel = drinkRepository.getReferenceById(id);
        return new DrinkDTO(
                drinkModel.getId(),
                drinkModel.getName(),
                drinkModel.getDescription(),
                drinkModel.getImage(),
                drinkModel.getImage_fit(),
                drinkModel.getCategory(),
                drinkModel.getSize(),
                drinkModel.getPrice());
    }

    public void saveDrink(CreateProductRequestDTO productRequestDTO) {
        List<DrinkModel> products = new ArrayList<>();
        productRequestDTO.getSizes().forEach((key, value) ->
                products.add(
                        new DrinkModel(
                                productRequestDTO.getName(),
                                DrinkCategory.valueOf("SODA"),
                                key,
                                productRequestDTO.getSizes().get(key),
                                productRequestDTO.getImageName(),
                                productRequestDTO.getImageFit(),
                                productRequestDTO.getDescription()
                        )
                ));
    }
}
