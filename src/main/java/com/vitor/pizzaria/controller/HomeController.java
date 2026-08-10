package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.PizzaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class HomeController {

    final private PizzaService pizzaService;

    HomeController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping("")
    public String getHomePage(Model model) {

        model.addAttribute("pizzas", pizzaService.getPizzas());
        model.addAttribute("price", "5.55");
        return "index";
    }
}
