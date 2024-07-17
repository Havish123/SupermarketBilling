package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletHistory {
    private int id;
    private int walletId;
    private int transactionType;
    private BigDecimal amount;
    private int createdBy;
    private LocalDateTime createdDate;
    private Wallet wallet;
    public WalletHistory() {
        wallet=new Wallet();
    }

    public WalletHistory(int id, int walletId, int transactionType, BigDecimal amount, int createdBy,
                         LocalDateTime createdDate) {
        this.id = id;
        this.walletId = walletId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

