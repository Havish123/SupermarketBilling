package views.User;

import DTO.UserDto;
import Extension.SecurityConfig;
import Services.UserServices;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class SignInPage {
    public static UserDto signIn() throws SQLException {
        try{
            UserServices userService = new UserServices();
            Scanner userInput = new Scanner(System.in);
            System.out.println("Sign In to your account");

            System.out.println("Please Choose Account Type.");
            System.out.println("1.Store User\n2.Customer");

            int userType=userInput.nextInt();

            String userSign="";
            if(userType==1 || userType ==2){
                userSign=userType==1?"admin":"customer";
            }else{
                System.out.println("Invalid Input");
                return null;
            }

            userInput.nextLine();
            System.out.println("Enter your email and press enter");
            String email = userInput.nextLine();
            UserDto user = null;
            if(email.isEmpty()) {
                return null;
            }else {
                user = userService.getUserByEmail(email,userSign);
                if(user == null){
                    System.out.println("Email does not exist");
                    return null;
                }
                else {
                    System.out.println("Enter your password and press enter");
                    String password = userInput.nextLine();
                    if(Objects.equals(password, user.getPassword())){
                        return user;
                    }
                    else {
                        System.out.println("Invalid password. Please try again");
                        return null;
                    }
                }
            }
        }
        catch (Exception e){
//            System.out.println("Error inside sign in "+ e.getMessage());
            throw e;
        }
    }
}
