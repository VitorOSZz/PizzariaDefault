package com.vitor.pizzaria.model.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        List<SizeResponse> sizes,
        String name,
        String description,
        // String image,
        int sort_order
) {
}
