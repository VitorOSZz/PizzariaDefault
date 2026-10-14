package com.vitor.pizzaria.model.dto;

import com.vitor.pizzaria.enums.ImageFitType;
import com.vitor.pizzaria.model.entity.Product;

import java.util.List;
import java.util.UUID;

public record SizeResponse(
        UUID id,
        List<ProductResponse> products,
        String name,
        // String description,
        String imageUrl,
        ImageFitType imageFit,
        int sort_order
) {
}
