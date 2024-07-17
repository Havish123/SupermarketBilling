package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerReportDto {
    private int CustomerId;
    private String Name;
    private double Amount;
    private String MobileNumber;
    private LocalDateTime JoinedDate;

    public LocalDateTime getJoinedDate() {
        return JoinedDate;
    }

    public void setJoinedDate(LocalDateTime joinedDate) {
        JoinedDate = joinedDate;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = JoinedDate.format(formatter);
        return formattedDateTime;
    }
    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-23s %-15s %-10.2f",CustomerId,Name,MobileNumber,Amount);
    }
}
