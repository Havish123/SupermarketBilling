
import Controller.HomeController;
import Controller.UserController;
import DTO.UserDto;
import Extension.DatabaseUtil;
import views.LandingPage;


public class Main {
    public static void main(String[] args) {
        try{
            DatabaseUtil.getConnection();

            while (true){
                System.out.println("Welcome to ShopStore");
                int choice=LandingPage.displayLoginMenu();
                handleSigning(choice);
                if(choice==0){
                    break;
                }
            };
        }catch (Exception e){
            System.out.println("Error in Application - "+e.getMessage());
        }

    }
    public static void handleSigning(int choice) throws Exception {
        try {
            UserController uc = new UserController();
            switch (choice){
                case 1: {
                    int registration = uc.signUp();
                    if(registration == 1){
                        System.out.println("Please sign in to your new account");
                        break;
                    }
                }
                break;
                case 2: {
                    UserDto currentUser = uc.signIn();
                    if(currentUser != null){
                        System.out.println("Login Successfully");
                        try(HomeController homepage=new HomeController()){
                            System.out.println("Home Controller Inside Try Block");
                            homepage.Initialize(currentUser);
                        };
                        break;
                    }
                }
                break;
                case 0:
                    return;
                default:{
                    System.out.println("Invalid Input.");
                    break;
                }
            }
        }
        catch (Exception e){
            throw e;
        }

    }




}
