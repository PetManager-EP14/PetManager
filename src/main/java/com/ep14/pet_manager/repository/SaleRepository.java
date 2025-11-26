package com.ep14.pet_manager.repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ep14.pet_manager.dto.SalesReportDetailDTO;
import com.ep14.pet_manager.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByUser_UserId(UUID userId);

    List<Sale> findByDateBetween(OffsetDateTime start, OffsetDateTime end);

    @Query("SELECT s FROM Sale s WHERE s.user.userId = :userId AND s.date BETWEEN :start AND :end")
    List<Sale> findByUserAndDateRange(@Param("userId") UUID userId,
                                      @Param("start") OffsetDateTime start,
                                      @Param("end") OffsetDateTime end);

    @Query("""
    SELECT new com.ep14.pet_manager.dto.SalesReportDetailDTO(
        p.name,
        SUM(sd.amount),
        SUM(sd.amount * p.priceSale)
    )
    FROM SaleDetails sd
    JOIN sd.sale s
    JOIN sd.product p
    WHERE s.createdAt BETWEEN :start AND :end
      AND s.status = com.ep14.pet_manager.entity.Sale.saleStatus.REGISTERED
    GROUP BY p.name
    """)
    List<SalesReportDetailDTO> getReportByDateRange(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end);

    // NUEVO MÉTODO CORREGIDO: Carga la Venta, sus Detalles y el Producto asociado en una sola consulta
    @Query("SELECT s FROM Sale s JOIN FETCH s.saleDetails sd JOIN FETCH sd.product")
    List<Sale> findAllWithDetailsAndProduct();
}