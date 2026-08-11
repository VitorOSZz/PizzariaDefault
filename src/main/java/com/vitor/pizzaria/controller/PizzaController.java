package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.model.entity.PizzaModel;
import com.vitor.pizzaria.service.PizzaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
class PizzaController {

    private final PizzaService pizzaService;

    PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("pizza")
    public String getChoicePizzaPage(@RequestParam(name="size") String size, Model model) {
        model.addAttribute("size", size);
        return "pizza";
    }
}
