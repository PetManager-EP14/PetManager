package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.PurchaseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(source = "purchaseId", target = "id")
    @Mapping(source = "supplier.supplierId", target = "supplierId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "purchaseDetails", target = "shoppingDetailIds", qualifiedByName = "detailsToIds")
    PurchaseDTO toDTO(Purchase purchase);

    @Mapping(source = "id", target = "purchaseId")
    @Mapping(source = "supplierId", target = "supplier.supplierId")
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "shoppingDetailIds", target = "purchaseDetails", qualifiedByName = "idsToDetails")
    Purchase toEntity(PurchaseDTO purchaseDTO);

    @Named("detailsToIds")
    public static List<Long> detailsToIds(List<PurchaseDetails> details) {
        if (details == null) return null;
        return details.stream()
                .map(PurchaseDetails::getPurchaseDetailId)
                .collect(Collectors.toList());
    }

    // Este método requiere acceso a un repositorio para buscar por ID, así que normalmente lo implementas en un mapper de servicio, no aquí.
    // Se deja como stub y puedes implementar la lógica fuera del mapper.
    @Named("idsToDetails")
    public static List<PurchaseDetails> idsToDetails(List<Long> ids) {
        // Aquí deberías usar un repositorio para buscar los objetos PurchaseDetails por sus IDs.
        // Ejemplo: purchaseDetailsRepository.findAllById(ids)
        return null;
    }
}