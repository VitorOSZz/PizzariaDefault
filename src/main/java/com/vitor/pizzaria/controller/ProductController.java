package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.model.dto.*;
import com.vitor.pizzaria.repository.SizeRepository;
import com.vitor.pizzaria.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/{slug}/api")
class ProductController {

    private final ProductService productService;
    ProductController(ProductService productService, SizeRepository sizeRepository) {
        this.productService = productService;
    }

    @GetMapping("/catalog")
    public List<CategoryResponse> getCatalog() {
        return productService.getCatalog();
    }

}
