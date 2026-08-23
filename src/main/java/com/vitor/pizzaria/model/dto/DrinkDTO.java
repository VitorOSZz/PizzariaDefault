package com.vitor.pizzaria.model.dto;

import com.vitor.pizzaria.enums.DrinkCategory;
import java.math.BigDecimal;

public class DrinkDTO extends Product {

    private final DrinkCategory category;
    private final String size;
    private final int price;

    public DrinkDTO(Long id, String name, String description, String imageName, String imageFit, DrinkCategory category, String size, Integer price) {
        super(id, name, description, imageName, imageFit);
        this.category = category;
        this.size = size;
        this.price = price;
    }

    public DrinkCategory getCategory() {
        return category;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }
}
