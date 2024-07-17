package DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyOrderDto {
    private int orderId;
    private int billnumber;
    private double totalAmount;
    private String orderType;
    private int totalCount;
    private double walletAmount;
    private double finalAmount;

    private LocalDateTime orderdate;

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        String formattedDateTime = orderdate.format(formatter);
        return formattedDateTime;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-23d %-15s %-25s %-15.2f %-15.2f %-15.2f",orderId,billnumber,orderType,this.getLocalDateString(),totalAmount,walletAmount,finalAmount);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(int billnumber) {
        this.billnumber = billnumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public LocalDateTime getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(LocalDateTime orderdate) {
        this.orderdate = orderdate;
    }
}
