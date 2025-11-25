package com.ep14.pet_manager.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SalesReportDTO;
import com.ep14.pet_manager.service.ReportExportService;
import com.ep14.pet_manager.service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;
    private final ReportExportService reportExportService;

    public SaleController(SaleService saleService, ReportExportService reportExportService) {
        this.saleService = saleService;
        this.reportExportService = reportExportService;
    }

    @PreAuthorize("hasAuthority('sale.create')")
    @PostMapping
    public ResponseEntity<SaleDTO> registerSale(@RequestBody SaleDTO saleDTO) {
        return ResponseEntity.ok(saleService.registerSale(saleDTO));
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('sale.read')")
    public ResponseEntity<List<SaleDTO>> getSalesByUser(@PathVariable UUID userId) {
        List<SaleDTO> sales = saleService.getSalesByUser(userId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('sale.read')")
    public ResponseEntity<List<SaleDTO>> getAllSalesFiltered(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long saleId) {
        List<SaleDTO> sales = saleService.getAllSalesFiltered(userId, startDate, endDate, saleId);
        return ResponseEntity.ok(sales);
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/daily")
    public ResponseEntity<SalesReportDTO> getDailyReport() {
        return ResponseEntity.ok(saleService.getDailyReport());
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/weekly")
    public ResponseEntity<SalesReportDTO> getWeeklyReport() {
        return ResponseEntity.ok(saleService.getWeeklyReport());
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/daily/export/excel")
    public ResponseEntity<byte[]> exportDailyReportToExcel() {
        try {
            SalesReportDTO report = saleService.getDailyReport();
            byte[] excelBytes = reportExportService.exportToExcel(report, "Diario");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reporte-diario.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/daily/export/pdf")
    public ResponseEntity<byte[]> exportDailyReportToPdf() {
        try {
            SalesReportDTO report = saleService.getDailyReport();
            byte[] pdfBytes = reportExportService.exportToPdf(report, "Diario");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte-diario.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/weekly/export/excel")
    public ResponseEntity<byte[]> exportWeeklyReportToExcel() {
        try {
            SalesReportDTO report = saleService.getWeeklyReport();
            byte[] excelBytes = reportExportService.exportToExcel(report, "Semanal");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reporte-semanal.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('sale.read')")
    @GetMapping("/report/weekly/export/pdf")
    public ResponseEntity<byte[]> exportWeeklyReportToPdf() {
        try {
            SalesReportDTO report = saleService.getWeeklyReport();
            byte[] pdfBytes = reportExportService.exportToPdf(report, "Semanal");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte-semanal.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
