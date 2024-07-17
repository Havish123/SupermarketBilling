package DTO;

public class EmpSalaryDto {

    private int customerId;
    private double amount;
    private String referenceKey;
    private double deductions;
    private double pfdeductions;
    private String PayMonth;

    public String getPayMonth() {
        return PayMonth;
    }

    public void setPayMonth(String payMonth) {
        PayMonth = payMonth;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getPfdeductions() {
        return pfdeductions;
    }

    public void setPfdeductions(double pfdeductions) {
        this.pfdeductions = pfdeductions;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }
}
