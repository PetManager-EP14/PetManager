package com.ep14.pet_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplier_products")
public class supplier_products {
    @ManyToOne
    @JoinColumn(name = "supplier_product")
    private suppliers suppliers;

    @ManyToOne
    @JoinColumn(name = "product")
    private product product;
}
