package com.vitor.pizzaria.model.entity;

import com.vitor.pizzaria.enums.PizzaSize;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="pizzas_price")
public class PizzaPriceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizza_price_id;

    @Column(name = "pizza_id")
    private Long pizza_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PizzaSize size;

    @Column(name = "price")
    private BigDecimal price;

    @Override
    public String toString() {
        return "PizzaPriceModel{" +
                "pizza_price_id=" + pizza_price_id +
                ", pizza_id=" + pizza_id +
                ", size=" + size +
                ", price=" + price +
                '}';
    }

    public Long getPizza_id() {
        return pizza_id;
    }

    public PizzaSize getSize() {
        return size;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
