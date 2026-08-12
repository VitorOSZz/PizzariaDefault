package com.vitor.pizzaria.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="pizzas")
public class PizzaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizza_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_name")
    private String image_name;

    @Override
    public String toString() {
        return "{" +
                "pizza_id=" + pizza_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_name='" + image_name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_name() {
        return image_name;
    }
}
