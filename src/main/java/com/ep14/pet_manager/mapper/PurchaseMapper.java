package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(source = "shopping_id", target = "id")
    @Mapping(source = "supplier.supplier_id", target = "supplierId")
    @Mapping(source = "user.user_id", target = "userId")
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    PurchaseDTO toDTO(Purchase purchase);

    @Mapping(source = "id", target = "shopping_id")
    @Mapping(source = "supplierId", target = "supplier.supplier_id")
    @Mapping(source = "userId", target = "user.user_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    Purchase toEntity(PurchaseDTO purchaseDTO);
}
