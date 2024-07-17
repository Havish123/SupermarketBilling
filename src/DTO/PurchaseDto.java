package DTO;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseDto {
    private int id;
    private LocalDateTime purchaseDate;
    private double amount;
    private int purchasedBy;
    private String DealerName;
    private String DealerContact;




    private List<PurchaseHistoryDto> purchaseList;

    public List<PurchaseHistoryDto> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<PurchaseHistoryDto> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDealerContact() {
        return DealerContact;
    }

    public void setDealerContact(String dealerContact) {
        DealerContact = dealerContact;
    }

    @Override
    public String toString() {
        return String.format("%-15d  %-25s  %-20s %-20s %-12.2f",id,purchaseDate,DealerName,DealerContact,amount);
    }
}
