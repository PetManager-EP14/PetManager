package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(source = "purchaseId", target = "id")
    @Mapping(source = "supplier.supplierId", target = "supplierId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    PurchaseDTO toDTO(Purchase purchase);

    @Mapping(source = "id", target = "purchaseId")
    @Mapping(source = "supplierId", target = "supplier.supplierId")
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Purchase toEntity(PurchaseDTO purchaseDTO);
}
