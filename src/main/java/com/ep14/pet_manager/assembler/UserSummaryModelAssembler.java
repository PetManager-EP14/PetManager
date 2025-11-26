package com.ep14.pet_manager.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ep14.pet_manager.controller.UserAdminController;
import com.ep14.pet_manager.dto.UserSummary;

@Component
public class UserSummaryModelAssembler implements RepresentationModelAssembler<UserSummary, EntityModel<UserSummary>> {

    @Override
    public EntityModel<UserSummary> toModel(UserSummary userSummary) {
        EntityModel<UserSummary> model = EntityModel.of(userSummary);
        
        // Add self link
        model.add(linkTo(methodOn(UserAdminController.class)
            .get(userSummary.userId()))
            .withSelfRel());
        
        return model;
    }
}