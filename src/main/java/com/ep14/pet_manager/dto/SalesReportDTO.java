package com.ep14.pet_manager.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDTO {
    private OffsetDateTime reportDate;
    private BigDecimal totalRevenue;
    private BigDecimal totalQuantitySold;
    private List<SalesReportDetailDTO> details;
}
