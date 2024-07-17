package DTO;

public class EmployeeSaleReport {
    private int EmployeeId;
    private  String EmployeeName;
    private  int BillCount;
    private double BillAmount;

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public int getBillCount() {
        return BillCount;
    }

    public void setBillCount(int billCount) {
        BillCount = billCount;
    }

    public double getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(double billAmount) {
        BillAmount = billAmount;
    }

    @Override
    public String toString() {
        return String.format("%-15d %-25s %-15d %-12.2f",EmployeeId,EmployeeName,BillCount,BillAmount);
    }
}
