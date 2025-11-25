package com.ep14.pet_manager.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ep14.pet_manager.dto.SalesReportDTO;
import com.ep14.pet_manager.dto.SalesReportDetailDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class ReportExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    private static final NumberFormat NUMBER_FORMATTER = NumberFormat.getNumberInstance(new Locale("es", "CO"));

    /**
     * Exporta un reporte de ventas a formato Excel
     */
    public byte[] exportToExcel(SalesReportDTO report, String reportType) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reporte de Ventas");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);

            int rowNum = 0;

            // Título
            Row titleRow = sheet.createRow(rowNum++);
            org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE DE VENTAS - " + reportType.toUpperCase());
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            // Fecha del reporte
            Row dateRow = sheet.createRow(rowNum++);
            dateRow.createCell(0).setCellValue("Fecha del reporte:");
            dateRow.createCell(1).setCellValue(report.getReportDate().format(DATE_FORMATTER));

            // Espacio
            rowNum++;

            // Resumen
            Row summaryHeaderRow = sheet.createRow(rowNum++);
            org.apache.poi.ss.usermodel.Cell summaryCell = summaryHeaderRow.createCell(0);
            summaryCell.setCellValue("RESUMEN GENERAL");
            summaryCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

            Row totalRevenueRow = sheet.createRow(rowNum++);
            totalRevenueRow.createCell(0).setCellValue("Ingresos Totales:");
            org.apache.poi.ss.usermodel.Cell revenueCell = totalRevenueRow.createCell(1);
            revenueCell.setCellValue(report.getTotalRevenue().doubleValue());
            revenueCell.setCellStyle(currencyStyle);

            Row totalQuantityRow = sheet.createRow(rowNum++);
            totalQuantityRow.createCell(0).setCellValue("Cantidad Total Vendida:");
            org.apache.poi.ss.usermodel.Cell quantityCell = totalQuantityRow.createCell(1);
            quantityCell.setCellValue(report.getTotalQuantitySold().doubleValue());
            quantityCell.setCellStyle(numberStyle);

            // Espacio
            rowNum++;

            // Cabecera de detalles
            Row detailsHeaderRow = sheet.createRow(rowNum++);
            org.apache.poi.ss.usermodel.Cell detailsCell = detailsHeaderRow.createCell(0);
            detailsCell.setCellValue("DETALLE POR PRODUCTO");
            detailsCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

            // Encabezados de columnas
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Producto", "Cantidad Vendida", "Ingresos"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            for (SalesReportDetailDTO detail : report.getDetails()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(detail.getProduct());
                
                org.apache.poi.ss.usermodel.Cell qtyCell = row.createCell(1);
                qtyCell.setCellValue(detail.getQuantitySold().doubleValue());
                qtyCell.setCellStyle(numberStyle);
                
                org.apache.poi.ss.usermodel.Cell revCell = row.createCell(2);
                revCell.setCellValue(detail.getRevenue().doubleValue());
                revCell.setCellStyle(currencyStyle);
            }

            // Ajustar anchos de columna
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exporta un reporte de ventas a formato PDF
     */
    public byte[] exportToPdf(SalesReportDTO report, String reportType) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("REPORTE DE VENTAS - " + reportType.toUpperCase())
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Fecha
            Paragraph date = new Paragraph("Fecha del reporte: " + report.getReportDate().format(DATE_FORMATTER))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(date);

            // Resumen
            Paragraph summaryTitle = new Paragraph("RESUMEN GENERAL")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10);
            document.add(summaryTitle);

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                    .setWidth(UnitValue.createPercentValue(60));

            summaryTable.addCell(createPdfCell("Ingresos Totales:", true));
            summaryTable.addCell(createPdfCell(CURRENCY_FORMATTER.format(report.getTotalRevenue()), false));

            summaryTable.addCell(createPdfCell("Cantidad Total Vendida:", true));
            summaryTable.addCell(createPdfCell(NUMBER_FORMATTER.format(report.getTotalQuantitySold()), false));

            document.add(summaryTable);

            // Detalles
            Paragraph detailsTitle = new Paragraph("DETALLE POR PRODUCTO")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(20);
            document.add(detailsTitle);

            Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2}))
                    .setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            detailsTable.addHeaderCell(createPdfHeaderCell("Producto"));
            detailsTable.addHeaderCell(createPdfHeaderCell("Cantidad Vendida"));
            detailsTable.addHeaderCell(createPdfHeaderCell("Ingresos"));

            // Datos
            for (SalesReportDetailDTO detail : report.getDetails()) {
                detailsTable.addCell(createPdfCell(detail.getProduct(), false));
                detailsTable.addCell(createPdfCell(NUMBER_FORMATTER.format(detail.getQuantitySold()), false));
                detailsTable.addCell(createPdfCell(CURRENCY_FORMATTER.format(detail.getRevenue()), false));
            }

            document.add(detailsTable);

            document.close();
            return out.toByteArray();
        }
    }

    // Métodos auxiliares para Excel
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }

    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    // Métodos auxiliares para PDF
    private com.itextpdf.layout.element.Cell createPdfCell(String content, boolean bold) {
        com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell().add(new Paragraph(content));
        if (bold) {
            cell.setBold();
        }
        cell.setPadding(5);
        return cell;
    }

    private com.itextpdf.layout.element.Cell createPdfHeaderCell(String content) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(content))
                .setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER);
    }
}
