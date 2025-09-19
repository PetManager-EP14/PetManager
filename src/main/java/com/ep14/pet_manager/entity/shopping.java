package com.ep14.pet_manager.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class shopping {
    @ManyToOne
    @JoinColumn(name = "shoppings")
    private suppliers suppliers;
}
