package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.model.dto.Product;
import com.vitor.pizzaria.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{type}/{option}/modal")
    public String getProductModalHeader(
            @PathVariable String type,
            @PathVariable String option) {
        return productService.getProductHeader(type, option);
    }

    @GetMapping("/{type}/{option}")
    public List<Product> getListOfProducts(
            @PathVariable String type,
            @PathVariable String option) {

        List<Product> products;
        products = productService.findProducts(type, option);

        return products;
    }
}
