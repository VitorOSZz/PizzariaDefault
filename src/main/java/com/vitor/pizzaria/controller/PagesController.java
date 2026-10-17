package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.tenants.TenantContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("")
class PagesController {

    private final ImageController imageController;

    public PagesController(ImageController imageController) {
        this.imageController = imageController;
    }


    @GetMapping("/")
    public String home() {
        return "redirect:/pizzaria-default/";
    }

    @GetMapping({"/{slug}", "/{slug}/"})
    public String getHomePage(@PathVariable String slug, Model model) {

        model.addAttribute("icon_telephone", imageController.getImage("icon-telephone.png"));
        model.addAttribute("icon_clock", imageController.getImage("icon-clock.png"));
        model.addAttribute("icon_cart", imageController.getImage("cart.webp"));
        model.addAttribute("icon_left_arrow", imageController.getImage("icon-left-arrow.webp"));

        return slug + "/index";
    }

    @GetMapping({"/{slug}/admin", "/{slug}/admin/"})
    public String getAdminPage(@PathVariable String slug) {
        return slug + "/admin";
    }

    @GetMapping({"/{slug}/checkout", "/{slug}/checkout/"})
    public String getCheckoutPage(@PathVariable String slug, Model model) {
        model.addAttribute("icon_left_arrow", imageController.getImage("icon-left-arrow.webp"));
        return slug + "/checkout";
    }

}
