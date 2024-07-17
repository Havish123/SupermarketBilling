package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductReportDto {
    private int Id;
    private String Name;
    private double Quantity;
    private LocalDateTime ExpiryDate;

    public LocalDateTime getExpiryDate() {
        return ExpiryDate;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        String formattedDateTime = ExpiryDate.format(formatter);
        return formattedDateTime;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        ExpiryDate = expiryDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }
    @Override
    public String toString(){
        return String.format("%-15d %-23s %-10.2f",Id,Name,Quantity);
    }
}
