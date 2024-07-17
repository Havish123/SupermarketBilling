package Services;

import Extension.DatabaseUtil;
import Extension.Roles;
import Extension.SecurityConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AuthenteService {

    public static AuthenteService _authenticateService=null;

    public static AuthenteService getInstance(){
        if(_authenticateService==null){
            _authenticateService=new AuthenteService();
        }
        return _authenticateService;
    }


    public boolean authenticate(Scanner scanner, int role) {
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        try (Connection conn = DatabaseUtil.getConnection()) {
            if(role== Roles.Customer.getValue()){
                String query = "SELECT * FROM Customers WHERE EmailId = ? AND Password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);
                    stmt.setString(2, SecurityConfig.EncryptData(password));
//                    stmt.setInt(3, role.equalsIgnoreCase("admin") ? 1 : 2);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("Authentication successful.");
                            do {
                                int id = rs.getInt("Id");
                                String name = rs.getString("Name");
//                                Configuration.userId=id;
//                                Configuration.roleId=Roles.Customer.getValue();
                                System.out.printf("Dear %s, Your Customer Id is - %d \n", name,id);
                                break;
                            }while (rs.next());

//                            Configuration.isAuthenticate=true;
                            return true;
                        } else {
                            System.out.println("Authentication failed.");
//                            Configuration.isAuthenticate=false;
                            return false;
                        }
                    }
                }
            }else{
                String query = "SELECT * FROM Employees WHERE EmailId = ? AND Password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);
                    stmt.setString(2, SecurityConfig.EncryptData(password));
//                    stmt.setInt(3, role);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int userId=rs.getInt("Id");
                            int roleId=rs.getInt("roleId");
                            System.out.println("Authentication successful.");
//                            Configuration.isAuthenticate=true;
//                            Configuration.userId=userId;
//                            Configuration.roleId=roleId;
                            return true;
                        } else {
                            System.out.println("Authentication failed.");
//                            Configuration.isAuthenticate=false;
                            return false;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public void changePassword(Scanner scanner) {
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Old Password: ");
        String oldPassword = scanner.nextLine();
        System.out.println("Enter New Password: ");
        String newPassword = scanner.nextLine();

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "UPDATE Customers SET Password = ? WHERE EmailId = ? AND Password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newPassword);
                stmt.setString(2, email);
                stmt.setString(3, oldPassword);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Password changed successfully.");
                } else {
                    System.out.println("Failed to change password. Please check your email and old password.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

