package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DayWiseSaleReport {
    private LocalDateTime SaleDate;
    private double TotalAmount;

    public LocalDateTime getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        SaleDate = saleDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        String formattedDateTime = SaleDate.format(formatter);
        return formattedDateTime;
    }
    @Override
    public String toString() {
        return String.format("%-25s %-12.2f",getLocalDateString(),TotalAmount);
    }
}
