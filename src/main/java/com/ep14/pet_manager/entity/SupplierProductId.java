package com.ep14.pet_manager.entity;

import java.io.Serializable;
import java.util.Objects;

public class SupplierProductId implements Serializable {
    private Long product_id;
    private Long supplier_id;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SupplierProductId))
            return false;
        SupplierProductId that = (SupplierProductId) o;
        return Objects.equals(product_id, that.product_id) && Objects.equals(supplier_id, that.supplier_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, supplier_id);
    }
}
