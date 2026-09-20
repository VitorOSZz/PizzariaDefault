package com.vitor.pizzaria.service.tenants;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantContext {

    private UUID tenantId;
    private String tenantSlug;

    public TenantContext() {
        this.tenantId = null;
        this.tenantSlug = null;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantSlug(String tenantSlug) {
        this.tenantSlug = tenantSlug;
    }

    public String getTenantSlug() {
        return tenantSlug;
    }

    public void clear() {
        this.tenantId = null;
    }
}
