package services.Impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.property.HorizontalAlignment;
import models.Sale;

import java.io.FileNotFoundException;
import java.util.List;

public class PDFGenerator {

    public static void generateSalesReport(List<Sale> sales, String dest, String fromDate, String toDate) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        // Add Title
        Paragraph title = new Paragraph("Sales Report")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setUnderline();
        document.add(title);

        // Add Description
        Paragraph description = new Paragraph("This document contains the sales report including service details and part sales details.")
                .setTextAlignment(TextAlignment.CENTER);
        document.add(description);

        // Add From Date and To Date
        Paragraph dateRange = new Paragraph("From Date: " + fromDate + " To Date: " + toDate)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(dateRange);

        // Add vertical space
        document.add(new Paragraph("\n"));

        // Add Service Details Title
        Paragraph serviceTitle = new Paragraph("Service details")
                .setFontSize(15)
                .setBold()
                .setUnderline();
        document.add(serviceTitle);

        // Add small vertical space
        document.add(new Paragraph("\n"));

        // Create Service Table
        Table serviceTable = new Table(2);
        serviceTable.setWidth(UnitValue.createPercentValue(100));
        serviceTable.addHeaderCell(new Cell().add(new Paragraph("Service Name").setBold()));
        serviceTable.addHeaderCell(new Cell().add(new Paragraph("Total Sales").setBold()));

        for (Sale sale : sales) {
            if (sale.isService()) {
                serviceTable.addCell(new Cell().add(new Paragraph(sale.getServiceName())));
                serviceTable.addCell(new Cell().add(new Paragraph(Double.toString(sale.getTotalSales()))));
            }
        }

        document.add(serviceTable);

        // Add vertical space
        document.add(new Paragraph("\n"));

        // Add Sales Details Title
        Paragraph salesTitle = new Paragraph("Sales details")
                .setFontSize(15)
                .setBold()
                .setUnderline();
        document.add(salesTitle);

        // Add small vertical space
        document.add(new Paragraph("\n"));

        // Create Sales Table
        Table salesTable = new Table(5);
        salesTable.setWidth(UnitValue.createPercentValue(100));
        salesTable.addHeaderCell(new Cell().add(new Paragraph("Part ID").setBold()));
        salesTable.addHeaderCell(new Cell().add(new Paragraph("Part Name").setBold()));
        salesTable.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold()));
        salesTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()));
        salesTable.addHeaderCell(new Cell().add(new Paragraph("Total Sales").setBold()));

        for (Sale sale : sales) {
            if (!sale.isService()) {
                salesTable.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getPartId()))));
                salesTable.addCell(new Cell().add(new Paragraph(sale.getPartName())));
                salesTable.addCell(new Cell().add(new Paragraph(Double.toString(sale.getUnitPrice()))));
                salesTable.addCell(new Cell().add(new Paragraph(Integer.toString(sale.getQuantity()))));
                salesTable.addCell(new Cell().add(new Paragraph(Double.toString(sale.getTotalSales()))));
            }
        }

        document.add(salesTable);

        document.close();
    }
}
