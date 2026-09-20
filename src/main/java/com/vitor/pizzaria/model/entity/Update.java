package com.vitor.pizzaria.model.entity;

import com.vitor.pizzaria.enums.UpdateAction;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "updates")
public class Update {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "update_id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "action", columnDefinition = "update_action", nullable = false)
    private UpdateAction action;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "row_before")
    private Map<String, UpdateAction> rowBefore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "row_after")
    private Map<String, UpdateAction> rowAfter;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UpdateAction getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = UpdateAction.valueOf(action);
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public Map<String, UpdateAction> getRowBefore() {
        return rowBefore;
    }

    public void setRowBefore(Map<String, UpdateAction> rowBefore) {
        this.rowBefore = rowBefore;
    }

    public Map<String, UpdateAction> getRowAfter() {
        return rowAfter;
    }

    public void setRowAfter(Map<String, UpdateAction> rowAfter) {
        this.rowAfter = rowAfter;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

}