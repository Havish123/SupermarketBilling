package Models;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private int id;
    private BigDecimal amount;
    private int modifiedBy;
    private LocalDateTime modifiedDate;
    private Customer customer;
    private List<WalletHistory> walletHistory;


    public Wallet() {
        walletHistory=new ArrayList<>();
        customer=new Customer();
    }

    public Wallet(int id,  BigDecimal amount, int modifiedBy, LocalDateTime modifiedDate) {
        this.id = id;
        this.amount = amount;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<WalletHistory> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(List<WalletHistory> walletHistory) {
        this.walletHistory = walletHistory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(int customerId) {
//        this.customerId = customerId;
//    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

