package views.User;

import DTO.EmpSalaryDto;
import DTO.PasswordChangeDto;
import DTO.UserDto;
import Extension.SecurityConfig;
import Models.Category;
import Models.Employee;
import Models.Product;

import java.util.List;
import java.util.Scanner;

public class AdminPage {

    public static void displayAdminCategoryMenu(){
        try {
            System.out.println("Enter your choice");
            System.out.println("1. Add new Category");
            System.out.println("2. Delete a Category");
            System.out.println("3. View Category");
            System.out.println("4. Exit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayAdminProductMenu(){
        try {
            System.out.println("Enter your choice");
            System.out.println("1. Add new Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete a Product");
            System.out.println("4. Show Product List");
            System.out.println("5. Exit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayAdminEmployeeMenu(){
        try {
            System.out.println("Enter your choice");
            System.out.println("1. Add new Employees");
            System.out.println("2. Update Employee Details");
            System.out.println("3. Delete a Employee");
            System.out.println("4. View Employee List");
            System.out.println("5. Exit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayAdminReportMenu(){
        try {
            System.out.println("1. Top Selling Products");
            System.out.println("2. Top 10 Customers");
            System.out.println("3. Customers who didn't buy products in last one month");
            System.out.println("4. Billing by Sales Persons");
            System.out.println("5. Days wise Revenue");
            System.out.println("6. Customers signup but did not make purchase");
            System.out.println("7. Products Need to Buy");
            System.out.println("8. Expired Products in Inventory");
            System.out.println("9. Products Not sold at All");
            System.out.println("10. Products that are out of Stock");
            System.out.println("0. Exit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Employee getEmployee(List<Employee> employees){
        Employee employee=null;
        Scanner userInput=new Scanner(System.in);
        displayEmployeeList(employees);
        while (true){
            System.out.println("Please Enter EmployeeId you want to update");
            int employeeId=userInput.nextInt();

            employee = employees.stream()
                    .filter(i -> i.getId() == employeeId)
                    .findFirst()
                    .orElse(null);
            if(employee==null){
                System.out.println("Please Enter valid EmployeeId");
            }else {
                break;
            }
        };
        return employee;
    }

    public static Employee updateEmployee(Employee employee){
        Scanner userInput=new Scanner(System.in);

        System.out.println("Enter EmployeeName("+employee.getName()+") : Press Enter to skip");
        String employeeName=userInput.nextLine();
        if(!employeeName.isEmpty()){
            employee.setName(employeeName);
        }

        System.out.println("Enter Employee EmailId("+employee.getEmailId()+") : Press Enter to skip");
        while (true){
            String emailId=userInput.nextLine();
            if(!emailId.isEmpty()){
                if(SecurityConfig.validateEmailId(emailId)){
                    employee.setEmailId(emailId);
                    break;
                }
                System.out.println("Please Enter valid EmailId. Press Enter to SKip");
            }
        }


        System.out.println("Enter Employee Mobilenumber("+employee.getMobileNumber()+") : Press Enter to skip");
        while (true){
            String mobilenumber=userInput.nextLine();
            if(!mobilenumber.isEmpty()){
                if(SecurityConfig.validateMobileNumber(mobilenumber)){
                    employee.setMobileNumber(mobilenumber);
                    break;
                }
                System.out.println("Please Enter Valid Mobile number. Press Enter to Skip");
            }
        };
        System.out.println("Enter New Password("+employee.getPassword()+") : Press Enter to skip");
        String password=userInput.nextLine();
        if(!password.isEmpty()){
            employee.setPassword(password);
        }
        return employee;
    }

    public static Employee addEmployee(){
        Employee employee=new Employee();
        Scanner userInput=new Scanner(System.in);

        System.out.println("Enter Name: ");
        employee.setName(userInput.nextLine());

        while (true){
            System.out.println("Enter Email: ");
            employee.setEmailId(userInput.nextLine());

            if(SecurityConfig.validateEmailId(employee.getEmailId())){
                break;
            }
            System.out.println("Please enter valid EmailId");
        };

        while(true){
            System.out.println("Enter Mobile Number: ");
            employee.setMobileNumber(userInput.nextLine());
            if(SecurityConfig.validateMobileNumber(employee.getMobileNumber())){
                break;
            }
            System.out.println("Please Enter Valid Mobilenumber");
        };

        System.out.println("Password Tip");
        System.out.println("-> at least 5 characters and at most 12 characters");
        System.out.println("-> at least one uppercase alphabet");
        System.out.println("-> at least one lowercase alphabet");
        System.out.println("-> at least one digit");
        System.out.println("-> at least one special character");

        while(true){
            System.out.println("Enter your Password: ");
            String password = userInput.nextLine();
            if(!SecurityConfig.validatePassword(password)){
                System.out.println("Your password is weak");
                continue;
            }
            System.out.println("Enter Confirm Password: ");
            String cnfPassword = userInput.nextLine();
            if(password.equals(cnfPassword)){
                employee.setPassword(password);
                break;
            }else{
                System.out.println("Both Password should be same");
            }
        };

        System.out.println("Enter Salary:");
        employee.setSalary(userInput.nextDouble());
        userInput.nextLine();

        return employee;
    }

    public static void displayEmployeeList(List<Employee> employees){
        System.out.println(String.format("%-15s  %-20s  %-15s %-20s %-20s %-10s %-10s","EmpId","Name","MobileNumber","EmailId","Password","Role","Status"));
        for(Employee employee :employees){
            System.out.println(employee.toString());
        }
    }

    public static void displaySalaryProcessed(List<EmpSalaryDto> employees){
        System.out.println(String.format("%-15s  %-20s  %-20s %-15s %-15s %-15s %-15s %-25s","EmpId","Pay Month","Basic Salary","Allowance","PF","Deductions","Net Salary","Payment Reference"));
        for(EmpSalaryDto employee :employees){
            System.out.println(String.format("%-15d  %-20s  %-19.2f %-14.2f %-14.2f %-14.2f %-14.2f %-25s",employee.getCustomerId(),employee.getPayMonth(),employee.getAmount()-3000,employee.getPfdeductions(),employee.getDeductions(),employee.getAmount()-employee.getPfdeductions()-employee.getDeductions(),employee.getReferenceKey()));
        }
    }

    public static PasswordChangeDto changePassword(PasswordChangeDto user){
        Scanner userInput=new Scanner(System.in);
        System.out.println("Password Tip");
        System.out.println("-> at least 5 characters and at most 12 characters");
        System.out.println("-> at least one uppercase alphabet");
        System.out.println("-> at least one lowercase alphabet");
        System.out.println("-> at least one digit");
        System.out.println("-> at least one special character");
        while (true){
            System.out.println("Enter your oldPassword");
            String oldPassword=userInput.nextLine();

            if(!oldPassword.equals(user.getOldPassword())){
                System.out.println("Invalid Old Password.Try again");
                continue;
            }

            while (true){
                System.out.println("Enter your New Password");
                String newPassword=userInput.nextLine();
                if(!SecurityConfig.validatePassword(newPassword)){
                    System.out.println("Your password is weak. Try with different password");
                    continue;
                }
                user.setNewPassword(newPassword);
                System.out.println("Enter Confirm Password:");
                String confirmPassword=userInput.nextLine();

                if(newPassword.equals(confirmPassword)){
                   break;
                }else{
                    System.out.println("Both Password should be same");
                }

            }
            break;
        };
        return user;
    }

}
