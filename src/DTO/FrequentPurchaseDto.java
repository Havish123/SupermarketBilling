package DTO;

public class FrequentPurchaseDto {
    private int ProductId;
    private String ProductName;
    private int PurchaseCount;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPurchaseCount() {
        return PurchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        PurchaseCount = purchaseCount;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-23s %-15d",ProductId,ProductName,PurchaseCount);
    }
}
