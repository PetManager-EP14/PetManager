package com.ep14.pet_manager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ep14.pet_manager.controller.NotificationController;
import com.ep14.pet_manager.entity.SaleNotification;

@Component
public class NotificationModelAssembler implements RepresentationModelAssembler<SaleNotification, EntityModel<SaleNotification>> {

    @Override
    public EntityModel<SaleNotification> toModel(SaleNotification notification) {
        EntityModel<SaleNotification> model = EntityModel.of(notification);
        
        // Add link to notifications by sale
        if (notification.getSale() != null && notification.getSale().getSaleId() != null) {
            model.add(linkTo(methodOn(NotificationController.class)
                .getNotificationsBySale(notification.getSale().getSaleId()))
                .withRel("sale-notifications"));
        }
        
        // Add link to notifications by type
        if (notification.getType() != null) {
            model.add(linkTo(methodOn(NotificationController.class)
                .getNotificationsByType(notification.getType()))
                .withRel("type-notifications"));
        }
        
        return model;
    }

    @Override
    public CollectionModel<EntityModel<SaleNotification>> toCollectionModel(Iterable<? extends SaleNotification> entities) {
        CollectionModel<EntityModel<SaleNotification>> collection = 
            RepresentationModelAssembler.super.toCollectionModel(entities);
        
        // Add self link to the collection
        collection.add(linkTo(methodOn(NotificationController.class)
            .getNotificationsByType(null))
            .withSelfRel());
        
        return collection;
    }
}
