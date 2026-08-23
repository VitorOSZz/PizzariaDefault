package com.vitor.pizzaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class AdminController {

    @GetMapping("admin")
    public String getHomePage() {
        return "admin-dashboard";
    }
}
