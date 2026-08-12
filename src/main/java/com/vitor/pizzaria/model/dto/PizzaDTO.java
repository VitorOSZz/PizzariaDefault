package com.vitor.pizzaria.model.dto;

import com.vitor.pizzaria.enums.PizzaSize;

import java.math.BigDecimal;

public class PizzaDTO extends Product{

    private final PizzaSize size;
    private final BigDecimal price;

    public PizzaDTO(Long id, String name, String description, String imageName, PizzaSize size, BigDecimal price) {
        super(id, name, description, imageName);
        this.size = size;
        this.price = price;
    }

    public PizzaSize getSize() {
        return size;
    }
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "PizzaDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
