package com.vitor.pizzaria.model.entity;

import com.vitor.pizzaria.enums.DrinkCategory;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Component;

@Entity
@Table(name="drinks")
public class DrinkModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "category")
    private DrinkCategory category;

    @Column(name = "size")
    private String size;

    @Column(name = "price")
    private Integer price;

    @Column(name = "image")
    private String image;

    @Column(name = "image_fit")
    private String image_fit;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DrinkCategory getCategory() {
        return category;
    }

    public String getSize() {
        return size;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getImage_fit() {
        return image_fit;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "DrinkModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
