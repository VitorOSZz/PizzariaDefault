package com.vitor.pizzaria.service.tenants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
class TenantFilter extends OncePerRequestFilter {

    TenantRegistry tenantRegistry;
    TenantContext tenantContext;

    public TenantFilter(TenantRegistry tenantRegistry, TenantContext tenantContext) {
        this.tenantRegistry = tenantRegistry;
        this.tenantContext = tenantContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String slug = request.getRequestURI().split("/")[1];
        System.out.println(request.getRequestURI() + " - Slug: " + slug);
        tenantContext.setTenantSlug(slug);

        UUID tenantId = tenantRegistry.getRestaurantId(slug);
        tenantContext.setTenantId(tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            tenantContext.clear();
        }
    }
}
