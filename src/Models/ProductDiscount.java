package Models;

import java.time.LocalDateTime;

public class ProductDiscount {

    private int id;
    private double percentage;
    private String description;
    private LocalDateTime validFromDate;
    private LocalDateTime validToDate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int createdBy;
    private int modifiedBy;
    private Product product;

    public ProductDiscount() {
        product=new Product();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

//    public int getProductId() {
//        return productId;
//    }
//
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }

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

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    @Override
    public String toString() {
        return String.format("%-15d  %-10d  %-10.2f %-25s %-20s %-20s", id, this.product.getId(), percentage, description, validFromDate, validToDate);
    }
}
