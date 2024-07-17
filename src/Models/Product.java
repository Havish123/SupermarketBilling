package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private Double price;
    private Double quantity;
    private boolean isActive;
    private LocalDateTime expiryDate;
    private Category category;

    private List<ProductDiscount> discountList;
    private List<ProductPurchaseHistory> purchaseHistory;

    public List<ProductDiscount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<ProductDiscount> discountList) {
        this.discountList = discountList;
    }

    public List<ProductPurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<ProductPurchaseHistory> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPrice(Double price) {
        this.price = price;
    }



    public Product() {
        category=new Category();
        discountList=new ArrayList<>();
        purchaseHistory=new ArrayList<>();
    }

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(int categoryId) {
//        this.categoryId = categoryId;
//    }

    public double getPrice() {
        return price;
    }
    public String getLocalDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string
        String formattedDateTime = "";
        if(expiryDate==null){
            formattedDateTime="Not Updated";
        }else{
            formattedDateTime = expiryDate.format(formatter);
        }
        return formattedDateTime;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString(){
        return String.format("%-15d %-23s %-15s %-15s %-10.2f %-10.2f %-25s",id,name,this.category.getCode(),this.category.getName(),price,quantity,getLocalDateString());
    }

//    public String getCategoryname() {
//        return categoryname;
//    }
//
//    public void setCategoryname(String categoryname) {
//        this.categoryname = categoryname;
//    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
