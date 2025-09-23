package com.ep14.pet_manager.entity;

import java.io.Serializable;
import java.util.Objects;

public class SupplierProductId implements Serializable {
    private Long productId;
    private Long supplierId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SupplierProductId))
            return false;
        SupplierProductId that = (SupplierProductId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(supplierId, that.supplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, supplierId);
    }
}
