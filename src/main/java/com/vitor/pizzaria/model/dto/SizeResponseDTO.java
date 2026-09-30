package com.vitor.pizzaria.model.dto;

import java.util.UUID;

public record SizeResponseDTO(
        UUID id,
        UUID categoryId,
        String name,
        Integer sortOrder,
        Boolean active
) {
}
