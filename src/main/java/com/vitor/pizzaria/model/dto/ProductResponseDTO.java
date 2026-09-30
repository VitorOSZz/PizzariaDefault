package com.vitor.pizzaria.model.dto;

import com.vitor.pizzaria.enums.ImageFitType;

import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        String description,
        String imageUrl,
        ImageFitType imageFit,
        boolean active,
        int sortOrder,
        CategoryResponseDTO category
) {}
