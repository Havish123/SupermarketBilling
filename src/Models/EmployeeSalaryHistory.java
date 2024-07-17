package Models;

import java.time.LocalDateTime;

public class EmployeeSalaryHistory {
    private int id;
    private double deductions;
    private double amount;
    private double pfdeductions;
    private String Paymonth;
    private int createdby;
    private LocalDateTime createddate;
    private String reference;
    private Employee employee;

    public EmployeeSalaryHistory() {
        employee=new Employee();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPfdeductions() {
        return pfdeductions;
    }

    public void setPfdeductions(double pfdeductions) {
        this.pfdeductions = pfdeductions;
    }

    public String getPaymonth() {
        return Paymonth;
    }

    public void setPaymonth(String paymonth) {
        Paymonth = paymonth;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
