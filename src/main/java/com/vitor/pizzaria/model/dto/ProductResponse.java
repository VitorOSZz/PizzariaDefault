package com.vitor.pizzaria.model.dto;

import com.vitor.pizzaria.enums.ImageFitType;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        Integer price,
        String imageUrl,
        ImageFitType imageFit,
        int sortOrder
) {
}
