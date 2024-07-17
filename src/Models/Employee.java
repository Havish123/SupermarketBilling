package Models;

import Extension.Roles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User {

    private boolean isActive;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdDate;
    private int createdBy;
    private LocalDateTime modifiedDate;
    private int modifiedBy;
    private double Salary;
    private Role role;

    private List<EmployeeSalaryHistory> employeeSalarylist;


    public List<EmployeeSalaryHistory> getEmployeeSalarylist() {
        return employeeSalarylist;
    }

    public void setEmployeeSalarylist(List<EmployeeSalaryHistory> employeeSalarylist) {
        this.employeeSalarylist = employeeSalarylist;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double salary) {
        Salary = salary;
    }

    public Employee() {
        role=new Role();
        employeeSalarylist=new ArrayList<>();
    }

    public Employee( boolean isActive, LocalDateTime lastLoginDate, LocalDateTime createdDate,
                    int createdBy, LocalDateTime modifiedDate, int modifiedBy) {

        this.isActive = isActive;
        this.lastLoginDate = lastLoginDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
    }
//    public int getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(int roleId) {
//        this.roleId = roleId;
//    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    @Override
    public String toString(){
        return String.format("%-15d %-23s %-15s %-20s %-20s %-10s %-10s",getId(),getName(),getMobileNumber(),getEmailId(),getPassword(), Roles.getNameByValue(this.role.getId()),isActive ? "Active":"Inactive");
    }
}

