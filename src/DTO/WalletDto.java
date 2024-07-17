package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WalletDto {
    private int Id;
    private int customerId;
    private String customerName;
    private double amount;
    private LocalDateTime lastmodifiedDate;



    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = lastmodifiedDate.format(formatter);
        return formattedDateTime;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getLastmodifiedDate() {
        return lastmodifiedDate;
    }

    public void setLastmodifiedDate(LocalDateTime lastmodifiedDate) {
        this.lastmodifiedDate = lastmodifiedDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
