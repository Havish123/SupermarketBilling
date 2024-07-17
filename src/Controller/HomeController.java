package Controller;

import DTO.PasswordChangeDto;
import DTO.UserDto;
import Extension.Roles;
import Models.*;
import views.Inventory.CategoryPage;
import views.Inventory.DiscountPage;
import views.Inventory.ProductDiscountPage;
import views.Inventory.ProductPage;
import views.LandingPage;
import views.Sales.BillingPage;
import views.User.AdminPage;
import views.User.CustomerPage;

import java.sql.SQLException;
import java.util.*;

public class HomeController implements AutoCloseable {
    boolean isLoggedIn = false;
    UserDto currentUser=null;
    int currentUserId=0;
    String currentUserRole;
    Scanner userInput = new Scanner(System.in);


    public HomeController() {
        System.out.println("Initialize Home Controller");
    }


    public void Initialize(UserDto newuser){
        try{
            this.currentUser=newuser;
            this.isLoggedIn = true;
            this.currentUserId = newuser.getUserId();
            this.currentUserRole = newuser.getRole();
            this.start();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void start() throws SQLException {
        try{
            do{
                LandingPage.displayMainMenu(isLoggedIn,currentUserRole,isLoggedIn?currentUser.getRoleId():0);
                try{
                    if(!isLoggedIn){
                        System.out.println("Please Sign in your account");
                    }
                    else {
                        int choice = userInput.nextInt();

                        if(currentUser.getRoleId()== Roles.Customer.getValue())
                            this.handleCustomer(choice);
                        else if(currentUser.getRoleId()==Roles.Cashier.getValue())
                            this.handleCashier(choice);
                        else if(currentUser.getRoleId()== Roles.Admin.getValue())
                            this.handleAdmin(choice);
                    }
                }catch (InputMismatchException e){
                    System.out.println("Invalid Input.Please choose correct Input");
                }

            }while (isLoggedIn);

        }
        catch (Exception e){
            throw e;
        }
    }

    public void handleCustomer(int choice){
        InventoryController controller=new InventoryController();
        SalesController salescontroler=new SalesController();
        UserController usercontrol=new UserController();
        switch (choice) {
            case 1:
                List<Category> categoryList=controller.getCategoryList();
                CategoryPage.displayCategoryListForCustomer(categoryList);
                int option=userInput.nextInt();
                if(option==0){
                    return;
                }else{
                    controller.showProductList(currentUser,option);
                    int productId=ProductPage.chooseProductPage();
                    if(productId!=0){
                        Product product=controller.getProductById(productId);
                        int buyoption= BillingPage.displayProductDetails(product);
                        if(buyoption!=0){
                            if(buyoption==1){
                                Cart cart=new Cart();
                                cart.getProduct().setId(productId);
                                cart.setPurchased(false);
                                cart.getCustomer().setId(currentUserId);
                                cart.setQuantity(1);
                                CartController cartcontrol=new CartController();
                                int result=cartcontrol.addCartItem(cart);
                                if (result == 1) {
                                    System.out.println("Products added in cart");
                                } else {
                                    System.out.println("Unable to process your request please try again.");
                                }
//                              controller.addToCart(currentUser,product);
                            }else if(buyoption==2){
                                Set<Integer> productIds=new HashSet<>();
                                productIds.add(product.getId());
                                salescontroler.checkoutProduct(productIds,currentUser);
                            }else {
                                System.out.println("Invalid Input, Try Again.");
                            }
                        }
                    }
                }
                break;
            case 2:
                salescontroler.showMyOrders(currentUserId);
                break;
            case 3:
                CartController cartController=new CartController();
                int cartchoice=cartController.showCartItemsByUserId(currentUserId);
                this.handleCartChoice(cartchoice);
                break;
            case 4:
                while (true){
                    int reportchoice=CustomerPage.displayReportMenu();

                    if(reportchoice==0){
                        break;
                    }
                    this.handleReportChoice(reportchoice);
                }

                break;
            case 5:
                usercontrol.showCustomerInfo(currentUserId);
                break;
            case 6:
                PasswordChangeDto passowrdDto=new PasswordChangeDto();
                passowrdDto.setOldPassword(currentUser.getPassword());
                passowrdDto.setUserId(currentUserId);
                int result=usercontrol.changeCustomerPassword(passowrdDto);
                if(result==1){
                    System.out.println("Password Changed Successfully");
                }else{
                    System.out.println("Password Change Failed");
                }
                break;
            case 0:
                this.logout();
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    public void handleReportChoice(int choice){
        ReportController controller=new ReportController();
        switch (choice){
            case 1:{
               controller.showFrequentBroughtItems(currentUserId);
            }
            break;
            case 2:
                controller.showBillStatementReport(currentUserId);
                break;
            case 3:
                controller.showLoyaltyPointsData(currentUserId);
                break;
            case 4:
                controller.showDiscountReport(currentUserId);
                break;
            case 5:
                controller.showWalletData(currentUserId);
                break;
            case 0:
                controller=null;
                return;
            default:
                controller=null;
                System.out.println("Invalid Input");
        }
    }
    public void handleCartChoice(int choice){
        switch (choice){
            case 1:{
                Set<Integer> cartIds=CustomerPage.getCartIds();
                SalesController controler=new SalesController();
                int result=controler.checkoutProductByCartIds(cartIds,currentUser);
                if(result>0){
                    System.out.println("Thank you for Purchase");
                }else{
                    System.out.println("Error while processing your request");
                }
            }
            break;
            case 2:{
                Set<Integer> cartIds=CustomerPage.getCartIds();
                CartController cartcontroler=new CartController();
                int rowsdeleted=cartcontroler.deleteCartItem(cartIds);
                if(rowsdeleted>0){
                    System.out.println("Deleted Successfully");
                }else{
                    System.out.println("Error while processing your request.");
                }
            }
            break;
            case 0:
                return;
            default:
                System.out.println("Invalid Input");
        }
    }
    public void logout(){
        isLoggedIn=false;
        currentUser=null;
        currentUserId=0;
        currentUserRole=null;
    }

    public void handleCashier(int choice){

        SalesController controller=new SalesController();
        switch (choice) {
            case 1:
                controller.billProduct(currentUserId);
                break;
            case 2:
                PasswordChangeDto passowrdDto=new PasswordChangeDto();
                passowrdDto.setOldPassword(currentUser.getPassword());
                passowrdDto.setUserId(currentUserId);
                UserController usercontrol=new UserController();
                int result=usercontrol.changeUserPassword(passowrdDto);
                if(result==1){
                    System.out.println("Password Changed Successfully");
                }else{
                    System.out.println("Password Change Failed");
                }
                break;
            case 0:
                this.logout();
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void handleAdmin(int choice) throws SQLException,InputMismatchException{
        switch (choice) {
            case 1:
                AdminPage.displayAdminProductMenu();
                int adminProductAction = userInput.nextInt();
                handleDisplayAdminProductMenu(adminProductAction);
                break;
            case 2:
                AdminPage.displayAdminCategoryMenu();
                int admincategoryAction = userInput.nextInt();
                handleDisplayAdminCategoryMenu(admincategoryAction);
                break;
            case 3:
                AdminPage.displayAdminEmployeeMenu();
                int adminEmployeeAction=userInput.nextInt();
                handleDisplayAdminEmployeeMenu(adminEmployeeAction);
                break;
            case 4:
                while (true){
                    AdminPage.displayAdminReportMenu();
                    int adminReportAction=userInput.nextInt();
                    if(adminReportAction==0){
                        break;
                    }
                    handleAdminReportMenu(adminReportAction);
                }

                break;
            case 5:
                PasswordChangeDto passowrdDto=new PasswordChangeDto();
                passowrdDto.setOldPassword(currentUser.getPassword());
                passowrdDto.setUserId(currentUserId);
                UserController controller=new UserController();
                int result=controller.changeUserPassword(passowrdDto);
                if(result==1){
                    System.out.println("Password Changed Successfully");
                }else{
                    System.out.println("Password Change Failed");
                }

                break;
            case 6:
                userInput.nextLine();
                NotificationController notification=new NotificationController();
                System.out.println("Enter your MailID:");
                String EmailId=userInput.nextLine();

                notification.SendEmailNotification(EmailId);
                break;
            case 7:
                DiscountPage.displayDiscountViewMenu();
                int discountoption=userInput.nextInt();
                handleDiscountMenu(discountoption);
                break;
            case 8:
                ProductDiscountPage.displayProductDiscountViewMenu();
                int admin=userInput.nextInt();
                handleProductDiscountMenu(admin);
                break;
            case 9:
                ProductDiscountPage.displayCouponMenu();
                int admin1=userInput.nextInt();
                handleCouponMenu(admin1);
                break;
            case 10:
                ProductDiscountPage.displayProductPurchaseMenu();
                int purchasechoice=userInput.nextInt();
                handleProductPurchaseMenu(purchasechoice);
                break;
            case 11:
                UserController usercontroller=new UserController();
                usercontroller.processEmployeeSalary(currentUserId);
                break;
            case 0:
                this.logout();
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void handleProductPurchaseMenu(int option){
        InventoryController controller=new InventoryController();
        switch (option){
            case 1:
                controller.addpurchase(currentUserId);
                break;
            case 2:
                controller.showPurchaseList(currentUserId);
                break;
            case 3:
                controller.showExpireProductList(currentUserId);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid Input");
                break;
        }
    }
    public void handleProductDiscountMenu(int option){
        DiscountController controller=new DiscountController();
        switch (option){
            case 1:
                controller.addProductDiscount(currentUserId);
                break;
            case 2:
                controller.updateProductDiscount(currentUserId);
                break;
            case 3:
                controller.viewProductDiscountList();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid Input");
        }
    }
    public void handleCouponMenu(int option){
        DiscountController controller=new DiscountController();
        switch (option){
            case 1:
                controller.addCoupon(currentUserId);
                break;
//            case 2:
//                controller.updateProductDiscount(currentUserId);
//                break;
            case 2:
                controller.showCouponList(currentUserId);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid Input");
        }
    }
    public void handleDiscountMenu(int option){
        DiscountController controller=new DiscountController();
        switch (option){
            case 1:
                controller.addDiscount(currentUserId);
                break;
            case 2:
                controller.updateDiscount(currentUserId);
                break;
            case 3:
                controller.viewDiscountList();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid Input");
        }
    }

    public void handleAdminReportMenu(int choice) throws SQLException,InputMismatchException{
        ReportController repocontrol=new ReportController();
        switch (choice){
            case 1:
                repocontrol.showTopSellingProducts();
                break;
            case 2:
                repocontrol.showTopTenCustomerList();
                break;
            case 3:
                repocontrol.showCustomerDidntButLastMonth(currentUserId);
                break;
            case 4:
                repocontrol.showEmployeeSaleReport(currentUserId);
                break;
            case 5:
                repocontrol.showSaleReport(currentUserId);
                break;
            case 6:
                repocontrol.showCustomersNotBuyAfterJoin(currentUserId);
                break;
            case 7:
                repocontrol.showProductsWtLowStock(currentUserId);
                break;
            case 8:
                repocontrol.showProductsExpiredButInventory(currentUserId);
                break;
            case 9:
                repocontrol.showProductsNotSoldAtAll(currentUserId);
                break;
            case 10:
                repocontrol.showProductsOutofStock(currentUserId);
                break;
            case 0:
                return;
            default:
                System.out.println("Please Enter valid Input");
        }
    }

    public void handleDisplayAdminEmployeeMenu(int choice) throws SQLException{
        UserController controller=new UserController();
        switch (choice) {
            case 1:
                int result=controller.addNewEmployee(currentUserId);
                if(result==1){
                    System.out.println("Employee Created Successfully");
                }else{
                    System.out.println("Failed to Create Employee");
                }
                break;
            case 2:
                int res=controller.updateEmployee(currentUserId);
                if(res==1){
                    System.out.println("Employee Update Successfully");
                }else{
                    System.out.println("Failed to update Employee");
                }
                break;
            case 3:
                int resul=0;
                if(resul==1){
                    System.out.println("Employee deleted successfully");
                }else{
                    System.out.println("Failed to delete employee");
                }
                break;
            case 4:
                controller.showEmployeeList();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void handleDisplayAdminProductMenu(int choice){
        InventoryController controller=new InventoryController();
        switch (choice) {
            case 1:
                this.handleAddProduct(controller);
                break;
            case 2:
                this.handleUpdateProduct(controller);
                break;
            case 3:
                this.handleProductDelete(controller);
                break;
            case 4:
                controller.showProductList(currentUser,0);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    public void handleDisplayAdminCategoryMenu(int choice){
        switch (choice) {
            case 1:
                Category newcategory=CategoryPage.addCategory();
                InventoryController controler=new InventoryController();
                int result=controler.addCategory(newcategory);
                if(result==1){
                    System.out.println("Added Successfully");
                }else{
                    System.out.println("Error in Category");
                }
                break;
            case 2:
                this.handleCategoryDelete();
                break;
            case 3:
                InventoryController controller=new InventoryController();
                List<Category> categoryList=controller.getCategoryList();
                CategoryPage.displayCategoryList(categoryList);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void handleCategoryDelete(){
        try{
            InventoryController controller=new InventoryController();
            int categoryId=CategoryPage.deleteCategory();

            int result=controller.deletecategory(categoryId);

            if(result==1){
                System.out.println("Category Deleted Successfully");
            }else{
                System.out.println("Category Not Found, Or Issues happen in Category delete.");
            }
        }catch (Exception e){

        }
    }

    public void handleProductDelete(InventoryController controller){
        try{
            int productId=ProductPage.deleteProduct();

            int result=controller.deleteproduct(productId);

            if(result==1){
                System.out.println("Product Deleted Successfully");
            }else{
                System.out.println("Product Not Found, Or Issues happen in product delete.");
            }
        }catch (Exception e){

        }
    }

    public void handleAddProduct(InventoryController controller){
        try{
            List<Category> categoryList=controller.getCategoryList();
            if(!categoryList.isEmpty()){
                Product product=new Product();
                while (true){
                    CategoryPage.displayCategoryList(categoryList);
                    System.out.println("Please Enter Category id");
                    int categoryId=userInput.nextInt();
                    userInput.nextLine();
                    Category ctg = categoryList.stream()
                            .filter(i -> i.getId() == categoryId)
                            .findFirst()
                            .orElse(null);
                    if (ctg == null) {
                        System.out.println("Please Select a valid Category");
                    }else{
                        product.getCategory().setId(categoryId);
                        break;
                    }
                };
                ProductPage.addProduct(product);
                int result=controller.addProduct(product);
                if(result==1){
                    System.out.println("Product Added Successfully");
                }else{
                    System.out.println("Something Error in the System, Please try again later");
                }
            }else{
                System.out.println("No Product Categories Found. Please Contact Administrator");
            }
        }catch (Exception e){
            System.out.println("Error in Product Add Page - "+e.getMessage());
        }
    }

    public void handleUpdateProduct(InventoryController controller) throws InputMismatchException{

        try{
            List<Product> productList=controller.showProductList(currentUser,0);
            if(!productList.isEmpty()){
                System.out.println("Enter Product Id :");
                int productId=userInput.nextInt();
                userInput.nextLine();

                Product product = productList.stream()
                        .filter(i -> i.getId() == productId)
                        .findFirst()
                        .orElse(null);
                if (product == null) {
                    System.out.println("Product Not available.");
                }else{
                    product.setId(productId);
                }
                ProductPage.updateProduct(product);

                int result=controller.updateProduct(product,currentUserId);
                if(result==1){
                    System.out.println("Product Update Successfully.");
                }else{
                    System.out.println("Product Update Failed.");
                }
            }else {
                System.out.println("No Products Available.");
            }
        }catch (InputMismatchException e){
           throw e;
        }catch (Exception e){
            System.out.println("Error in Product Update");
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("Home Controller Destroyed or Closed");
    }
}
