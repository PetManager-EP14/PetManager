package com.ep14.pet_manager.entity;

import java.io.Serializable;
import java.util.Objects;

public class supplier_product_id implements Serializable {
    private Long product_id;
    private Long supplier_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof supplier_product_id)) return false;
        supplier_product_id that = (supplier_product_id) o;
        return Objects.equals(product_id, that.product_id) && Objects.equals(supplier_id, that.supplier_id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(product_id, supplier_id);
    }
}
