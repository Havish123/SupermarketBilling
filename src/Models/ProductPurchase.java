package Models;

import java.time.LocalDateTime;

public class ProductPurchase {
    private int Id;
    private LocalDateTime purchaseDate;
    private double amount;
    private int purchasedBy;
    private String DealerEmail;
    private String DealerMobileNumber;
    private LocalDateTime createddate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(int purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public String getDealerEmail() {
        return DealerEmail;
    }

    public void setDealerEmail(String dealerEmail) {
        DealerEmail = dealerEmail;
    }

    public String getDealerMobileNumber() {
        return DealerMobileNumber;
    }

    public void setDealerMobileNumber(String dealerMobileNumber) {
        DealerMobileNumber = dealerMobileNumber;
    }

    public LocalDateTime getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDateTime createddate) {
        this.createddate = createddate;
    }
}
