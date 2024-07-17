package views.User;

import DTO.MyOrderDto;
import DTO.WalletDto;
import DTO.WalletHistoryDto;
import Models.Cart;
import Models.Customer;

import java.util.*;

public class CustomerPage {

    public static Customer updateCustomerPage(Customer customer){
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Customer Name("+customer.getName()+") : (press Enter to skip) ");
        String customerName=userInput.nextLine();
        if(!customerName.isEmpty()){
            customer.setName(customerName);
        }

        System.out.println("Enter MobileNumber("+customer.getMobileNumber()+"): (press Enter to skip)");
        String mobilenumber=userInput.nextLine();
        if(!mobilenumber.isEmpty()){
            customer.setMobileNumber(mobilenumber);
        }

        System.out.println("Enter Email Id("+customer.getEmailId()+"):(press Enter to skip)");
        String emailId=userInput.nextLine();
        if(!emailId.isEmpty()){
            customer.setEmailId(emailId);
        }

        System.out.println("Enter Address ("+customer.getAddress()+"):(press Enter to skip)");
        String address=userInput.nextLine();
        if(!address.isEmpty()){
            customer.setAddress(address);
        }

        return  customer;
    }

    public static void displayMyOrders(List<MyOrderDto> orders,int totalpages,int currentpage){

        if(orders.size()>0){
            System.out.println("Total Pages: "+ totalpages+", Current Page: "+(currentpage+1));

            System.out.println(String.format("%-15s %-23s %-12s %-25s %-12s %-15s %-15s","OrderId","Billnumber","OrderType","OrderDate","TotalAmount","Wallet Amount","Final Amount"));
            for(MyOrderDto order: orders){
                System.out.println(order.toString());
            }
        }else{
            System.out.println("No Record found");
        }

    }

    public static int displayWalletHistoryMenu(int totalpage,int currentpage){
        try{
            Scanner userInput=new Scanner(System.in);
            if(totalpage==1){
                System.out.println("0 - Exit");
            }else if(currentpage+1==1){
                System.out.println("1. Next Page");
                System.out.println("0 - Exit");
            }else if(currentpage+1==totalpage){
                System.out.println("2. Previous Page");
                System.out.println("0 - Exit");
            }
            else{
                System.out.println("1. Next Page");
                System.out.println("2. Previous Page");
                System.out.println("0 - Exit");
            }
            System.out.println("Enter the option:");
            int choice=userInput.nextInt();

            return choice;
        }catch (InputMismatchException e){
            System.out.println("Invalid Input.");
            return 0;
        }
    }

    public static Double getwalletTopup(double walletamount){
        Scanner userInput=new Scanner(System.in);

        System.out.println("Enter the amount you want to add: Total Balance: "+walletamount);

        double topupbalance=userInput.nextDouble();
        return topupbalance;
    }

    public static int displayOrdersMenu(int totalpage,int currentpage){
        try{
            Scanner userInput=new Scanner(System.in);
            if(currentpage+1==1){
                System.out.println("1. Next Page");
                System.out.println("3. View Order Details");
                System.out.println("0 - Exit");
            }else if(currentpage+1==totalpage){
                System.out.println("2. Previous Page");
                System.out.println("3. View Order Details");
                System.out.println("0 - Exit");
            }else{
                System.out.println("1. Next Page");
                System.out.println("2. Previous Page");
                System.out.println("3. View Order Details");
                System.out.println("0 - Exit");
            }
            System.out.println("Enter the option:");
            int choice=userInput.nextInt();

            return choice;
        }catch (InputMismatchException e){
            System.out.println("Invalid Input.");
            return 0;
        }

    }

    public static Set<Integer> getCartIds(){
        Set<Integer> cartIds=new HashSet<>();
        Scanner userInput=new Scanner(System.in);
        try{
            System.out.println("Enter Cart Ids like 1,2,..");
            String cartidstring=userInput.nextLine();
            if(!cartidstring.isEmpty()){
                String[] array=cartidstring.split(",");
                for(String num:array){
                    try{
                        cartIds.add(Integer.parseInt(num));

                    }catch (NumberFormatException e){
                        System.out.println("Invalid Input,Try again later.");
                    }
                }
            }else{
                System.out.println("Invalid Input");
            }
        }
        catch (Exception e){
            System.out.println("Error Processing your request");
        }
        return cartIds;
    }

    public static int showCartList(List<Cart> cartlist){
        int choice=0;
        Scanner userInput=new Scanner(System.in);
        try{
            if(cartlist.size()>0){
                System.out.println("Cart List");
                System.out.println(String.format("%-15s %-15s %-23s %-10s %-10s","CartId","ProductId","ProductName","Quantity","Price"));
                for(Cart cart:cartlist){
                    System.out.println(cart.toString());
                }

                System.out.println("1. Checkout CartItems");
                System.out.println("2. Delete CartItems");
                System.out.println("0. for Exit");

                choice=userInput.nextInt();
            }else{
                System.out.println("No Products found");
                return 0;
            }


        }catch (Exception e){
            System.out.println("Error while processing your request");
        }

        return choice;
    }

    public static int displayReportMenu(){
        Scanner userInput=new Scanner(System.in);
        System.out.println("Enter your choice");
        System.out.println("1. Top 10 Frequently brought Items");
        System.out.println("2. Show last 10 Bills");
        System.out.println("3. Loyalty Points");
        System.out.println("4. Discount Report");
        System.out.println("5. Wallet Details");
        System.out.println("0. Exit");

        return userInput.nextInt();
    }

    public static void displayWalletDetails(List<WalletHistoryDto> inputDto,int totalpages,int currentpage){
        System.out.println("Wallet History");
        Scanner userInput=new Scanner(System.in);
        if(inputDto.size()>0){
            System.out.println("Total Pages: "+ totalpages+", Current Page: "+(currentpage+1));

            System.out.println(String.format("%-15s %-15s %-25s %-15s","TransactionId","TransactionType","TransactionDate","Amount"));
            for(WalletHistoryDto wallet: inputDto){
                System.out.println(wallet.toString());
            }
        }else{
            System.out.println("No Record found");
        }

    }

    public static int displyaCustomerProfile(Customer customer){
        Scanner userinput=new Scanner(System.in);
        System.out.println();
        System.out.println("Profile Page");
        System.out.println(String.format("%-15s %-25s","Name :",customer.getName()));
        System.out.println(String.format("%-15s %-25s","EmailId :",customer.getEmailId()));
        System.out.println(String.format("%-15s %-25s","MobileNumber :",customer.getMobileNumber()));

        System.out.println("1. Wallet Info");
        System.out.println("2. Update Profile");
        System.out.println("0. Exit");
        System.out.println("Enter the Option");

        int choice=userinput.nextInt();

        return choice;

    }

    public static int displayWalletInfo(WalletDto input) throws InputMismatchException{
        Scanner userInput=new Scanner(System.in);
        System.out.println();
        System.out.println("Wallet Info");
        System.out.println("Amount: "+input.getAmount());
        System.out.println("Last Recharge Date: "+ input.getLocalDateString());

        System.out.println("1. Wallet History");
        System.out.println("2. Topup Wallet");
        System.out.println("0. Exit");
        System.out.println("Enter your Option");
        int choice=userInput.nextInt();

        return choice;
    }
}
