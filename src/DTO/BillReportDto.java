package DTO;

public class BillReportDto {
    private int OrderId;
    private int customerId;
    private String customerName;
    private  double discountAmount;
    private double totalAmount;
    private double finalAmount;
    private boolean isFromWallet;
    private double walletAmount;
    private int billnumber;

    public int getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(int billnumber) {
        this.billnumber = billnumber;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
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

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public boolean isFromWallet() {
        return isFromWallet;
    }

    public void setFromWallet(boolean fromWallet) {
        isFromWallet = fromWallet;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-15d %-20s %-15.2f %-19.2f %-19.2f %-19.2f",OrderId,billnumber,customerName,discountAmount,totalAmount,finalAmount,walletAmount);
    }
}
