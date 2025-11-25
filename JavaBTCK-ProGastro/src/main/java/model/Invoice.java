package model;

import java.io.FileWriter;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class Invoice {

    private String orderId;
    private String customerName;
    private String createdAt;
    private String status;
    private String note;
    private double totalPrice;

    private List<OrderItem> items;

    public Invoice(){

    }

    public Invoice(String orderId, String customerName, String createdAt, String status, String note, List<OrderItem> items, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.status = status;
        this.note = note;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public String getNote() { return note; }
    public double getTotalPrice() { return totalPrice; }
    public List<OrderItem> getItems() { return items; }

    public String generateInvoiceText() {
        StringBuilder sb = new StringBuilder();

        sb.append("==================================================\n");
        sb.append("                    HÓA ĐƠN ĐẶT MÓN\n");
        sb.append("==================================================\n");
        sb.append("Mã đơn hàng: ").append(orderId).append("\n");
        sb.append("Khách hàng: ").append(customerName).append("\n");
        sb.append("Ngày đặt: ").append(createdAt).append("\n");
        sb.append("Trạng thái: ").append(status).append("\n");
        sb.append("Ghi chú: ").append(note).append("\n\n");

        sb.append("----------------------- MÓN ĂN --------------------\n");
        sb.append("Tên món             SL     Giá        Thành tiền\n");
        sb.append("--------------------------------------------------\n");

        for (OrderItem item : items) {
            double subtotal = item.getPrice() * item.getQuantity();

            sb.append(String.format("%-18s %-6d %-10.1f %.1f\n",
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    subtotal));
        }

        sb.append("\n--------------------------------------------------\n");
        sb.append(String.format("TỔNG CỘNG: %40.1f\n", totalPrice));
        sb.append("==================================================\n");
        sb.append("                CẢM ƠN QUÝ KHÁCH\n");
        sb.append("==================================================\n");

        return sb.toString();
    }

    public void saveToTXT() {
        try {
            String fileName = "invoice_" + orderId + ".txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(generateInvoiceText());
            writer.close();
            System.out.println("Đã tạo file TXT: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveToExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Invoice");

            int rowIndex = 0;

            // Header thông tin hóa đơn
            sheet.createRow(rowIndex++).createCell(0).setCellValue("HÓA ĐƠN ĐẶT MÓN");
            sheet.createRow(rowIndex++).createCell(0).setCellValue("Mã đơn hàng: " + orderId);
            sheet.createRow(rowIndex++).createCell(0).setCellValue("Khách hàng: " + customerName);
            sheet.createRow(rowIndex++).createCell(0).setCellValue("Ngày đặt: " + createdAt);
            sheet.createRow(rowIndex++).createCell(0).setCellValue("Trạng thái: " + status);
            sheet.createRow(rowIndex++).createCell(0).setCellValue("Ghi chú: " + note);

            rowIndex++; // dòng trống

            // Tiêu đề bảng món ăn
            Row header = sheet.createRow(rowIndex++);
            header.createCell(0).setCellValue("Tên món");
            header.createCell(1).setCellValue("Số lượng");
            header.createCell(2).setCellValue("Giá");
            header.createCell(3).setCellValue("Thành tiền");

            // Ghi dữ liệu món ăn
            for (OrderItem item : items) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(item.getName());
                row.createCell(1).setCellValue(item.getQuantity());
                row.createCell(2).setCellValue(item.getPrice());
                row.createCell(3).setCellValue(item.getPrice() * item.getQuantity());
            }

            // Tổng cộng
            Row totalRow = sheet.createRow(rowIndex++);
            totalRow.createCell(2).setCellValue("Tổng cộng:");
            totalRow.createCell(3).setCellValue(totalPrice);

            // Auto-size column
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "invoice_" + orderId + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Đã tạo file Excel: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
