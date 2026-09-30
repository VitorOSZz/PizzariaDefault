package com.vitor.pizzaria.model.dto;

import java.util.UUID;

public record CategoryResponseDTO(
        UUID id,
        String name,
        String description,
        Boolean active,
        int sort_order
) {}
