package DTO;

public class WalletTopUpDto {

    private int userId;
    private int walletId;
    private double walletamount;
    private int transactionType;
    private double BalanceAmount;

    public double getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public double getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(double walletamount) {
        this.walletamount = walletamount;
    }
}
