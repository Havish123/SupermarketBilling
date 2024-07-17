package views;

import Extension.Roles;

import java.util.Scanner;

public class LandingPage {
    public static int displayLoginMenu(){
        Scanner userInput=new Scanner(System.in);
        System.out.println("Enter Your Choice");
        System.out.println("1. Sign Up");
        System.out.println("2. Sign In");
        System.out.println("0. Close");
        int choice=userInput.nextInt();

        return choice;
    }
    public static void displayMainMenu(boolean isLoggedIn, String currentUserRole,int RoleId){
        try{
            if(!isLoggedIn){
//                System.out.println("Enter Your Choice");
//                System.out.println("1. Sign Up");
//                System.out.println("2. Sign In");
//                System.out.println("3. Close");
                System.out.println("Please SignIn your account");
            }
            else {
                if(RoleId== Roles.Cashier.getValue()){
                    displayCashierMenu();
                }else if(RoleId==Roles.Admin.getValue()){
                    displayAdminMenu();
                }else if(RoleId==Roles.Customer.getValue()){
                    displayCustomerMenu();
                }
            }
        }
        catch (Exception e){
            throw e;
        }
    }
    public static void displayCashierMenu(){
        System.out.println("Staff Menu");
        System.out.println("1. New Bill");
        System.out.println("2. Change Password");
        System.out.println("0. Logout");
    }

    public static void displayAdminMenu(){
        System.out.println("1. Product");
        System.out.println("2. Category");
        System.out.println("3. Employees");
        System.out.println("4. Report");
        System.out.println("5. Change Password");
        System.out.println("6. Test Gmail");
        System.out.println("7. Discount");
        System.out.println("8. Product Discount");
        System.out.println("9. Coupon");
        System.out.println("10. Product Purchase");
        System.out.println("11. Process Salary");
        System.out.println("0. Logout");
    }

    public static void displayCustomerMenu(){
        System.out.println("1. Category List");
        System.out.println("2. My Orders");
        System.out.println("3. My Cart");
        System.out.println("4. Report");
        System.out.println("5. My Profile");
        System.out.println("6. Change Password");
        System.out.println("0. Logout");
    }



}
