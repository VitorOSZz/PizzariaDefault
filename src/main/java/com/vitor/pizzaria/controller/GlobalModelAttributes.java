package com.vitor.pizzaria.controller;

import com.vitor.pizzaria.service.tenants.TenantContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final TenantContext tenantContext;

    public GlobalModelAttributes(TenantContext tenantContext) {
        this.tenantContext = tenantContext;
    }

    @ModelAttribute
    public void addTenantAttributes(Model model) {
        model.addAttribute("slug", tenantContext.getTenantSlug());
    }
}