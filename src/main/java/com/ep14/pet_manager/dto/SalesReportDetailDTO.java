package com.ep14.pet_manager.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDetailDTO {
    private String product;
    private BigDecimal quantitySold;
    private BigDecimal revenue;
}