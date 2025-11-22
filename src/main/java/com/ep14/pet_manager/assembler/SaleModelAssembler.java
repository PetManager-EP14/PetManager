package com.ep14.pet_manager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ep14.pet_manager.controller.SaleController;
import com.ep14.pet_manager.dto.SaleDTO;

@Component
public class SaleModelAssembler implements RepresentationModelAssembler<SaleDTO, EntityModel<SaleDTO>> {

    @Override
    public EntityModel<SaleDTO> toModel(SaleDTO sale) {
        EntityModel<SaleDTO> model = EntityModel.of(sale);
        
        // Add self link
        model.add(linkTo(methodOn(SaleController.class)
            .getSaleById(sale.getSaleId()))
            .withSelfRel());
        
        // Add link to all sales
        model.add(linkTo(methodOn(SaleController.class)
            .getAllSales())
            .withRel("sales"));
        
        // Add link to user's sales if userId is present
        if (sale.getUserId() != null) {
            model.add(linkTo(methodOn(SaleController.class)
                .getSalesByUser(sale.getUserId()))
                .withRel("user-sales"));
        }
        
        return model;
    }

    @Override
    public CollectionModel<EntityModel<SaleDTO>> toCollectionModel(Iterable<? extends SaleDTO> entities) {
        CollectionModel<EntityModel<SaleDTO>> collection = 
            RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Add self link to the collection
        collection.add(linkTo(methodOn(SaleController.class)
            .getAllSales())
            .withSelfRel());
        
        return collection;
    }
}
