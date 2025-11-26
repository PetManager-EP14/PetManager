package com.ep14.pet_manager.mapper;

import com.ep14.pet_manager.dto.PurchaseDTO;
import com.ep14.pet_manager.dto.PurchaseDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Purchase;
import com.ep14.pet_manager.entity.PurchaseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    // 1. Mapeo de Entidad (Purchase) a DTO (PurchaseDTO)
    @Mappings({
        @Mapping(source = "purchaseId", target = "id"),
        // Mapea la relación n-1 al ID simple
        @Mapping(source = "supplier.supplierId", target = "supplierId"),
        @Mapping(source = "user.userId", target = "userId"),
        @Mapping(source = "createdAt", target = "createdAt"),
        @Mapping(source = "updatedAt", target = "updatedAt"),
        // Mapeo CLAVE: Transfiere la lista de entidades de detalles a la lista 'details' del DTO
        @Mapping(source = "purchaseDetails", target = "details") 
    })
    PurchaseDTO toDTO(Purchase purchase);

    // 2. Mapeo de DTO (PurchaseDTO) a Entidad (Purchase)
    @Mappings({
        @Mapping(source = "id", target = "purchaseId"),
        @Mapping(source = "supplierId", target = "supplier.supplierId"),
        @Mapping(source = "userId", target = "user.userId"),
        @Mapping(source = "createdAt", target = "createdAt"),
        @Mapping(source = "updatedAt", target = "updatedAt"),
        @Mapping(target = "purchaseDetails", ignore = true) // Ignorar, la lógica de persistencia va en el Service
    })
    Purchase toEntity(PurchaseDTO purchaseDTO);

    // 3. Mapeo de Detalle (PurchaseDetails Entity) a DTO (PurchaseDetailDTO)
    // Esto extrae los campos anidados (Product) para que el DTO sea plano
    @Mappings({
        @Mapping(source = "product.productId", target = "productId"),
        @Mapping(source = "amount", target = "amount"),
        @Mapping(source = "purchaseDetailId", target = "purchaseDetailId"),
        @Mapping(source = "product.name", target = "productName"), // Extrae el nombre del producto
        // Asume que priceShopping de Product es el precio unitario de la compra
        @Mapping(source = "product.priceShopping", target = "unitPrice") 
    })
    PurchaseDetailDTO toDTO(PurchaseDetails entity);

    // 4. Mapeo de DTO (PurchaseDetailDTO) a Detalle (PurchaseDetails Entity)
    @Mappings({
        @Mapping(source = "productId", target = "product"),
        @Mapping(target = "purchase", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "purchaseDetailId", ignore = true)
    })
    PurchaseDetails toEntity(PurchaseDetailDTO dto);


    // 5. Métodos Auxiliares/Helper
    // Helper para mapear Long productId <-> Product entity
    default Product map(Long productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setProductId(productId);
        return p;
    }

    // Helper para mapear Product -> Long
    default Long map(Product product) {
        return product == null ? null : product.getProductId();
    }
    
    // Mapeo de enum de estado
    Purchase.statusShopping mapStatus(PurchaseDTO.StatusShopping status);
}