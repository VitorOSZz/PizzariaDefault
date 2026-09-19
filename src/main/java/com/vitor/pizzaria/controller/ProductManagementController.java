package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.model.dto.CreateProductRequestDTO;
import com.vitor.pizzaria.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/products")
class ProductManagementController {

    private final ProductService productService;

    public ProductManagementController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public void addProduct(
            @RequestBody CreateProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
    }
}
