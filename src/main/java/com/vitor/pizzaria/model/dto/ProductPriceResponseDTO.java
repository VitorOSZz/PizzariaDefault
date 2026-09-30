package com.vitor.pizzaria.model.dto;

import java.util.UUID;

public record ProductPriceResponseDTO(
        UUID productPriceId,
        UUID productId,
        UUID sizeId,
        boolean active,
        int price
) {}
