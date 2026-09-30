package com.vitor.pizzaria.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_prices")
public class ProductPrice {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "product_price_id", nullable = false)
    private UUID id;

    @JoinColumn(name = "product_id", nullable = false)
    private UUID productId;

    @JoinColumn(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @JoinColumn(name = "size_id", nullable = false)
    private UUID sizeId;

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_update_id")
    private Update lastUpdate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurant(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public UUID getSizeId() {
        return sizeId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Update getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Update lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "id=" + id +
                ", productId=" + productId +
                ", restaurantId=" + restaurantId +
                ", sizeId=" + sizeId +
                ", active=" + active +
                ", priceCents=" + priceCents +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}