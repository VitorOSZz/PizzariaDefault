package com.vitor.pizzaria.model.dto;

import java.util.HashMap;

public class CreateProductRequestDTO extends Product{

    private final String status;
    private final String type;
    private HashMap<String, Integer > sizes = new HashMap<>();

    public CreateProductRequestDTO(Long id, String name, String description, String imageName, String imageFit, String status, String type, HashMap<String, Integer> sizes) {
        super(id, name, description, imageName, imageFit);
        this.status = status;
        this.type = type;
        this.sizes = sizes;
    }

    public HashMap<String, Integer> getSizes() {
        return sizes;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CreateProductRequestDTO{" +
                "id=" + id +
                ", sizes=" + sizes +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageFit='" + imageFit + '\'' +
                '}';
    }
}
