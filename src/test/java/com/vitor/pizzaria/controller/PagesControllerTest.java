package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.tenants.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PagesControllerTest {

    @Autowired
    PagesController pagesController;

    @Autowired
    TenantContext tenantContext;

    private String slug;
    private Model model;

    @BeforeEach
    void setUp() {
        slug = "pizzaria-default";
        model = new ExtendedModelMap();
        tenantContext.setTenantSlug(slug);
    }

    @Test
    void getHomePage() {
        String homePage = pagesController.getHomePage(slug, model);
        System.out.println(homePage);

        assertEquals("pizzaria-default/index", homePage);
    }

    @Test
    void getAdminPage() {
        String adminPage = pagesController.getAdminPage(slug);
        System.out.println(adminPage);

        assertEquals("pizzaria-default/admin", adminPage);
    }

    @Test
    void getCheckoutPage() {
        String checkoutPage = pagesController.getCheckoutPage(slug, model);
        System.out.println(checkoutPage);

        assertEquals("pizzaria-default/checkout", checkoutPage);
    }
}