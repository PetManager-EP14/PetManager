package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.DTO.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);
    PurchaseDTO toDTO(Purchase customer);
    Purchase toEntity(PurchaseDTO customerDTO);
}
