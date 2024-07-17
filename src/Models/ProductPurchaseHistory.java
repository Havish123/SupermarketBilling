package Models;

import java.time.LocalDateTime;

public class ProductPurchaseHistory {
    private int id;
    private double quantity;
    private double amount;
    private LocalDateTime expiryDate;
    private boolean isExpired;
    private double saleprice;
    private double availablequantity;

    private Product product;
    private ProductPurchase purchase;

    public ProductPurchaseHistory() {
        product=new Product();
        purchase=new ProductPurchase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(double saleprice) {
        this.saleprice = saleprice;
    }

    public double getAvailablequantity() {
        return availablequantity;
    }

    public void setAvailablequantity(double availablequantity) {
        this.availablequantity = availablequantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductPurchase getPurchase() {
        return purchase;
    }

    public void setPurchase(ProductPurchase purchase) {
        this.purchase = purchase;
    }
}
