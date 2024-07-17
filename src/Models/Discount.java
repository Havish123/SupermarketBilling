package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Discount {
    private int id;
    private String discountType;
    private double percentage;
    private String description;
    private LocalDateTime validFromDate;
    private LocalDateTime validToDate;
    private LocalDateTime createdDate;
    private int createdBy;
    private LocalDateTime modifiedDate;
    private int modifiedBy;
    private int minPurchaseAmount;

    public int getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(int minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public Discount() {
    }

    public Discount(int id, String discountType, double percentage, String description,
                    LocalDateTime validFromDate, LocalDateTime validToDate, LocalDateTime createdDate,
                    int createdBy, LocalDateTime modifiedDate, int modifiedBy) {
        this.id = id;
        this.discountType = discountType;
        this.percentage = percentage;
        this.description = description;
        this.validFromDate = validFromDate;
        this.validToDate = validToDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(LocalDateTime validFromDate) {
        this.validFromDate = validFromDate;
    }

    public LocalDateTime getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(LocalDateTime validToDate) {
        this.validToDate = validToDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    @Override
    public String toString() {
        return String.format("%-15d  %-20s  %-10.2f %-25s %-20s %-20s", id, discountType, percentage, description, validFromDate, validToDate);
    }
}

