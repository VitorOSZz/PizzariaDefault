package com.vitor.pizzaria.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class LoginPageController {

    @GetMapping("/login")
    public String getLoginPage(Authentication authentication, Model model) {
        return renderLoginPage(authentication, model, "/pizzaria-default/admin");
    }

    @GetMapping("/{slug}/login")
    public String getTenantLoginPage(
            @PathVariable String slug,
            Authentication authentication,
            Model model
    ) {
        return renderLoginPage(authentication, model, "/" + slug + "/admin");
    }

    private String renderLoginPage(
            Authentication authentication,
            Model model,
            String redirectUrl
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:" + redirectUrl;
        }

        return "universal/auth";
    }
}
