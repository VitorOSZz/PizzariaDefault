package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("finalizar-pedido")
    public String endOrder() {
        return "finalizar-pedido";
    }
}
