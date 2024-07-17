package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Coupon {
    private int id;
    private double value;
    private String description;
    private boolean isUsed;
    private LocalDateTime validfromdate;
    private LocalDateTime validtodate;
    private  int createdby;
    private LocalDateTime createddate;
    private String Code;
    private int totalclaims;
    private int availableclaims;
    private double minamount;


    public Coupon() {

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public LocalDateTime getValidfromdate() {
        return validfromdate;
    }

    public void setValidfromdate(LocalDateTime validfromdate) {
        this.validfromdate = validfromdate;
    }

    public LocalDateTime getValidtodate() {
        return validtodate;
    }

    public void setValidtodate(LocalDateTime validtodate) {
        this.validtodate = validtodate;
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

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getTotalclaims() {
        return totalclaims;
    }

    public void setTotalclaims(int totalclaims) {
        this.totalclaims = totalclaims;
    }

    public int getAvailableclaims() {
        return availableclaims;
    }

    public void setAvailableclaims(int availableclaims) {
        this.availableclaims = availableclaims;
    }

    public double getMinamount() {
        return minamount;
    }

    public void setMinamount(double minamount) {
        this.minamount = minamount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

