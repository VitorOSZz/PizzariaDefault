package com.vitor.pizzaria.model.dto;

public class Product {

    final Long id;
    final String name;
    final String description;
    final String imageName;
    final String imageFit;

    public Product(Long id, String name, String description, String imageName, String imageFit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.imageFit = imageFit;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImageName() {
        return imageName;
    }
    public String getImageFit() {
        return imageFit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageFit='" + imageFit + '\'' +
                '}';
    }
}
