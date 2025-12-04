package com.sleekydz86.server.market.product.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum ProductStatus {

    WAITING,
    RESERVED,
    COMPLETED;

    public boolean isCompleted() {
        return this.equals(COMPLETED);
    }
}