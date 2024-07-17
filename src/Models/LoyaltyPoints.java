package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoyaltyPoints {
    private int id;
    private int pointsearned;
    private int pointsspent;
    private int createdby;
    private int modifiedby;
    private LocalDateTime modifieddate;

    private Customer customer;
    private List<LoyaltyPointHistory> loyaltypointhistories;

    public LoyaltyPoints() {
        loyaltypointhistories=new ArrayList<>();
    }

    public List<LoyaltyPointHistory> getLoyaltypointhistories() {
        return loyaltypointhistories;
    }

    public void setLoyaltypointhistories(List<LoyaltyPointHistory> loyaltypointhistories) {
        this.loyaltypointhistories = loyaltypointhistories;
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

    public int getPointsspent() {
        return pointsspent;
    }

    public void setPointsspent(int pointsspent) {
        this.pointsspent = pointsspent;
    }

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public int getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(int modifiedby) {
        this.modifiedby = modifiedby;
    }

    public LocalDateTime getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(LocalDateTime modifieddate) {
        this.modifieddate = modifieddate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
