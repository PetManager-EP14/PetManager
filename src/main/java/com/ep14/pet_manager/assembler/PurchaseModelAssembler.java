package com.ep14.pet_manager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ep14.pet_manager.controller.PurchaseController;
import com.ep14.pet_manager.dto.PurchaseDTO;

@Component
public class PurchaseModelAssembler implements RepresentationModelAssembler<PurchaseDTO, EntityModel<PurchaseDTO>> {

    @Override
    public EntityModel<PurchaseDTO> toModel(PurchaseDTO purchase) {
        EntityModel<PurchaseDTO> model = EntityModel.of(purchase);
        
        // Add self link
        model.add(linkTo(methodOn(PurchaseController.class)
            .getPurchaseById(purchase.getId()))
            .withSelfRel());
        
        // Add link to all purchases
        model.add(linkTo(methodOn(PurchaseController.class)
            .getAllPurchases())
            .withRel("purchases"));
        
        return model;
    }

    @Override
    public CollectionModel<EntityModel<PurchaseDTO>> toCollectionModel(Iterable<? extends PurchaseDTO> entities) {
        CollectionModel<EntityModel<PurchaseDTO>> collection = 
            RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Add self link to the collection
        collection.add(linkTo(methodOn(PurchaseController.class)
            .getAllPurchases())
            .withSelfRel());
        
        return collection;
    }
}
