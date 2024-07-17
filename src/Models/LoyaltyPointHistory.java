package Models;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.time.LocalDateTime;

public class LoyaltyPointHistory {
    private int id;
    private int pointsearned;
    private int transactiontype;
    private int createdby;
    private LocalDateTime createddate;
    private Order order;
    private LoyaltyPoints loyaltypoint;

    public LoyaltyPointHistory() {
        order=new Order();
        loyaltypoint=new LoyaltyPoints();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPointsearned() {
        return pointsearned;
    }

    public void setPointsearned(int pointsearned) {
        this.pointsearned = pointsearned;
    }

    public int getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(int transactiontype) {
        this.transactiontype = transactiontype;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public LocalDateTime getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDateTime createddate) {
        this.createddate = createddate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LoyaltyPoints getLoyaltypoint() {
        return loyaltypoint;
    }

    public void setLoyaltypoint(LoyaltyPoints loyaltypoint) {
        this.loyaltypoint = loyaltypoint;
    }
}
