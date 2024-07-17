package views.User;

import Extension.SecurityConfig;
import Models.Customer;

import java.util.Scanner;

public class SignupPage {
    public static Customer SignupUser(Customer customer){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Welcome to SignUp");
        System.out.println("Enter Name: ");
        customer.setName(scanner.nextLine());

        while (true){
            System.out.println("Enter Email: ");
            customer.setEmailId(scanner.nextLine());

            if(SecurityConfig.validateEmailId(customer.getEmailId())){
                break;
            }
            System.out.println("Please enter valid EmailId");
        };

        while(true){
            System.out.println("Enter Mobile Number: ");
            customer.setMobileNumber(scanner.nextLine());
            if(SecurityConfig.validateMobileNumber(customer.getMobileNumber())){
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
            String password = scanner.nextLine();
            if(!SecurityConfig.validatePassword(password)){
                System.out.println("Your password is weak");
                continue;
            }
            System.out.println("Enter Confirm Password: ");
            String cnfPassword = scanner.nextLine();
            if(password.equals(cnfPassword)){
                customer.setPassword(password);
                break;
            }else{
                System.out.println("Both Password should be same");
            }
        };

        System.out.println("Enter Address: ");
        customer.setAddress(scanner.nextLine());

        return  customer;
    }


}
