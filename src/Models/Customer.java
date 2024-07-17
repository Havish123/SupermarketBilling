package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private boolean isActive;
    private String address;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isSubscribeEmails;
    private LoyaltyPoints loyaltyPoint;
    private List<Cart> cartList;

    public Customer() {
        cartList=new ArrayList<>();
        loyaltyPoint=new LoyaltyPoints();
    }

    public Customer(boolean isActive, String address, LocalDateTime lastLoginDate, LocalDateTime createdDate,
                    LocalDateTime modifiedDate, boolean isSubscribeEmails) {

        this.isActive = isActive;
        this.address = address;
        this.lastLoginDate = lastLoginDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isSubscribeEmails = isSubscribeEmails;
    }

    public LoyaltyPoints getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(LoyaltyPoints loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
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

    public boolean isSubscribeEmails() {
        return isSubscribeEmails;
    }

    public void setSubscribeEmails(boolean subscribeEmails) {
        isSubscribeEmails = subscribeEmails;
    }
}

