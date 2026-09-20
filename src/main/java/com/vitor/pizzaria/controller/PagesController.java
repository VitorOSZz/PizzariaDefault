package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.tenants.TenantContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
class PagesController {



    @GetMapping({"/{slug}", "/{slug}/"})
    public String getHomePage(@PathVariable String slug) {
        return slug + "/index";
    }

    @GetMapping({"/{slug}/admin", "/{slug}/admin/"})
    public String getAdminPage(@PathVariable String slug) {
        return slug + "/admin";
    }

    @GetMapping({"/{slug}/checkout", "/{slug}/checkout/"})
    public String getCheckoutPage(@PathVariable String slug) {
        return slug + "/checkout";
    }

}
