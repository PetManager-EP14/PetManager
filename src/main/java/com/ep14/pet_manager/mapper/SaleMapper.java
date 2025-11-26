package com.ep14.pet_manager.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.SaleDetails;
import com.ep14.pet_manager.entity.User; 

@Mapper(componentModel = "spring")
public interface SaleMapper {

    // Sale (Entity) -> SaleDTO (DTO)
    @Mapping(source = "saleId", target = "saleId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.name", target = "customerName")
    @Mapping(source = "saleDetails", target = "details")
    SaleDTO toDTO(Sale sale); 

    // SaleDTO -> Sale (sin cambios)
    @Mapping(source = "userId", target = "user.userId")
    @Mapping(target = "saleDetails", ignore = true)
    @Mapping(target = "saleNotification", ignore = true)
    Sale toEntity(SaleDTO dto); 

    // Element mapping: SaleDetailDTO -> SaleDetails 
    @Mapping(source = "productId", target = "product")
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SaleDetails toEntity(SaleDetailDTO dto); 

    // Element mapping: SaleDetails (Entity) -> SaleDetailDTO (DTO)
    @Mapping(source = "product.productId", target = "productId")    
    @Mapping(source = "amount", target = "amount")             
    @Mapping(source = "saleDetailId", target = "saleDetailId") 
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.priceSale", target = "unitPrice") 
    SaleDetailDTO toDTO(SaleDetails entity);

    // Helper to map productId <-> Product 
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
}
