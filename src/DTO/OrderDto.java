package DTO;

import Models.Customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderDto{
    private int Id;
    public double totalAmount;
    private int currentUserId;
    public int orderType;
    private boolean isFromWallet;
    private  double walletAmount;
    private double finalamount;
    private int billnumber;
    private int discountId;
    private double discountAmount;
    private int walletId;
    private LocalDateTime orderdate;
    private String orderTypeString;
    public Customer customer;
    public List<OrderDetailDto> orderdetails;
    private double BalanceWallet;
    private String CouponCode;
    private boolean IsCouponApplied;
    private int CouponId;
    private int AvailableCouponClaims;
    private double CouponAmount;
    private String referenceKey;

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public double getCouponAmount() {
        return CouponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        CouponAmount = couponAmount;
    }

    public int getAvailableCouponClaims() {
        return AvailableCouponClaims;
    }

    public void setAvailableCouponClaims(int availableCouponClaims) {
        AvailableCouponClaims = availableCouponClaims;
    }

    public int getCouponId() {
        return CouponId;
    }

    public void setCouponId(int couponId) {
        CouponId = couponId;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public boolean isCouponApplied() {
        return IsCouponApplied;
    }

    public void setCouponApplied(boolean couponApplied) {
        IsCouponApplied = couponApplied;
    }

    public double getBalanceWallet() {
        return BalanceWallet;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setBalanceWallet(double balanceWallet) {
        BalanceWallet = balanceWallet;
    }

    public OrderDto(){
        orderdetails=new ArrayList<>();
    }

    public int getWalletId() {
        return walletId;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public boolean isFromWallet() {
        return isFromWallet;
    }

    public double getFinalamount() {
        return finalamount;
    }

    public void setFinalamount(double finalamount) {
        this.finalamount = finalamount;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getOrderdate() {
        return orderdate;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = orderdate.format(formatter);
        return formattedDateTime;
    }
    public void setOrderdate(LocalDateTime orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderTypeString() {
        return orderTypeString;
    }

    public void setOrderTypeString(String orderTypeString) {
        this.orderTypeString = orderTypeString;
    }

    public int getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(int billnumber) {
        this.billnumber = billnumber;
    }
}
