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

    @Column(name = "image_fit")
    private String image_fit;

    @Override
    public String toString() {
        return "{" +
                "pizza_id=" + pizza_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_name='" + image_name + '\'' +
                '}';
    }

    public Long getPizza_id() {
        return pizza_id;
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

    public String getImage_fit() {
        return image_fit;
    }

    protected PizzaModel() {}

    public PizzaModel(String name, String description, String image_name, String image_fit) {
        this.name = name;
        this.description = description;
        this.image_name = image_name;
        this.image_fit = image_fit;
    }
}
