package Services;

import Extension.DatabaseUtil;
import Extension.SecurityConfig;
import Models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerService {
    static Connection conn = DatabaseUtil.getConnection();

    public int getCustomerIdBasedOnMobilenumber(String mobileNumber) {
        try  {
            String query = "SELECT Id,Name FROM Customers WHERE mobilenumber = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, mobileNumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("Id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCustomerIdBasedOnEmailId(String emailId) {

        try{
            String query = "SELECT Id,Name FROM Customers WHERE emailId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, emailId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("Id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getWalletAmount(int customerId) {
        try  {
            String query = "SELECT amount FROM wallets WHERE customerid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, customerId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("amount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createNewCustomer(Customer customer) {
        if(customer.getPassword()==null){
            customer.setPassword("1234");
        }
        try{
            String query = "INSERT INTO Customers (Name, EmailId, MobileNumber, Password, IsActive, Address, CreatedDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, customer.getName());
                stmt.setString(2, customer.getEmailId());
                stmt.setString(3, customer.getMobileNumber());
                stmt.setString(4, SecurityConfig.EncryptData(customer.getPassword()));
                stmt.setBoolean(5, true);
                stmt.setString(6, customer.getAddress());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  -1;
    }

    public void CreateWalletAccount(int customerId){
        try {
            String query = "INSERT INTO wallets (customerId, amount, modifiedBy,modifiedDate) VALUES (?, ?, ?,NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, 0);
                stmt.setInt(3, customerId);

                stmt.executeUpdate();
                System.out.println("Wallet Account Created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public Boolean IsExit(String input){
        if(input.toLowerCase().equals("exit")){
            return true;
        }else{
            return  false;
        }
    }

    public void signUpCustomer(Scanner scanner) {
        Customer customer=new Customer();
        System.out.println("Welcome to SignUp");
        System.out.println("Type 'exit' for Close");

        System.out.println("Enter Name: ");
        customer.setName(scanner.nextLine());

        if(IsExit(customer.getName())){
            return;
        }

        while (true){
            System.out.println("Enter Email: ");
            customer.setEmailId(scanner.nextLine());

            if(IsExit(customer.getEmailId())){
                return;
            }
            if(SecurityConfig.validateEmailId(customer.getEmailId())){
                break;
            }
            System.out.println("Please enter valid EmailId");
        };

        while(true){
            System.out.println("Enter Mobile Number: ");
            customer.setMobileNumber(scanner.nextLine());
            if(IsExit(customer.getMobileNumber())){
                return;
            }
            int customerId=getCustomerIdBasedOnMobilenumber(customer.getMobileNumber());
            if(customerId<=0){
                if(SecurityConfig.validateMobileNumber(customer.getMobileNumber())){
                    break;
                }
                System.out.println("Please Enter Valid Mobilenumber");
            }else{
                System.out.println("Mobile Number Already Exists");
            }

        };
        while(true){
            System.out.println("Enter Password: ");
            String password = scanner.nextLine();

            if(IsExit(password)){
                return;
            }
            if(!SecurityConfig.validatePassword(password)){
                System.out.println("Password contains atleat one UpperCase,Number,Special Character and length Greater than 5 and less than 12");
                continue;
            }
            System.out.println("Enter Confirm Password: ");
            String cnfPassword = scanner.nextLine();

            if(IsExit(cnfPassword)){
                return;
            }
            if(password.equals(cnfPassword)){
                customer.setPassword(password);
                break;
            }else{
                System.out.println("Both Password should be same");
            }
        };

        System.out.println("Enter Address: ");
        customer.setAddress(scanner.nextLine());
        if(IsExit(customer.getAddress())){
            return;
        }

        int customerId=createNewCustomer(customer);

        if(customerId==-1){
            System.out.println("Something went wrong Please try again later");
        }else{
            System.out.println("SignUp Successfully");
            CreateWalletAccount(customerId);
        }
    }
}
