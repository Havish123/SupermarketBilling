package DTO;

import Extension.TransactionType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WalletHistoryDto {
    private int id;
    private int tustomerId;
    private String customerName;
    private  int transactionId;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionDate;
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTustomerId() {
        return tustomerId;
    }

    public void setTustomerId(int tustomerId) {
        this.tustomerId = tustomerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        String formattedDateTime = transactionDate.format(formatter);
        return formattedDateTime;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-15s %-25s %-12.2f",id, transactionType,this.getLocalDateString(),amount);
    }
}
