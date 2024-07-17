package views.Inventory;

import DTO.PurchaseDto;
import DTO.PurchaseHistoryDto;
import Models.Category;
import Models.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ProductPage {
    public static Product addProduct(Product product){
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Product Name: ");
        product.setName(userInput.nextLine());
        System.out.println("Enter Price: ");
        product.setPrice(userInput.nextDouble());
        System.out.println("Enter Quantity: ");
        product.setQuantity(userInput.nextDouble());
        userInput.nextLine();

        return  product;
    }
    public static Product updateProduct(Product product){

        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Product Name("+product.getName()+") : (press Enter to skip) ");
        String productname=userInput.nextLine();
        if(!productname.isEmpty()){
            product.setName(productname);
        }

        System.out.println("Enter Price("+String.valueOf(product.getPrice())+"): (press Enter to skip)");
        String price=userInput.nextLine();
        if(!price.isEmpty()){
            product.setPrice(Double.parseDouble(price));
        }

//        System.out.println("Enter Quantity("+String.valueOf(product.getQuantity())+"):(press Enter to skip)");
//        String quantity=userInput.nextLine();
//        if(!quantity.isEmpty()){
//            product.setQuantity(Double.parseDouble(quantity));
//        }
        return  product;
    }

    public static int deleteProduct(){
        int productId=0;
        Scanner scanner=new Scanner(System.in);

        System.out.println("Enter product Id: ");
        productId=scanner.nextInt();

        return productId;
    }
    public static void displayProductViewMenu(){
        System.out.println("1. View All Products");
        System.out.println("2. View Products by Price Order");
        System.out.println("3. View Products by Category");
        System.out.println("4. Search Products");
        System.out.println("5. Exit");
    }
    public static void displayProducts(List<Product> productList){
        System.out.println(String.format("%-15s  %-20s  %-15s %-15s %-10s %-10s %-25s","ProductId","Name","CategoryCode","Category","Price","Quantity","Expiry Date"));
        for(Product product :productList){
            System.out.println(product.toString());
        }
    }

    public static void displayPurchaseList(List<PurchaseDto> productList){
        System.out.println(String.format("%-15s  %-25s  %-20s %-20s %-13s","Purchase Id","Date","Dealer Email","Dealer Contact","Amount"));
        for(PurchaseDto product :productList){
            System.out.println(product.toString());
        }
    }

    public static void displayExpiryPurchaseList(List<PurchaseDto> productList){
        System.out.println(String.format("%-15s  %-25s  %-20s %-20s %-13s","Purchase Id","Date","Dealer Email","Dealer Contact","Amount"));
        for(PurchaseDto product :productList){
            System.out.println(product.toString());
        }
    }

    public static void displayProductListForBilling(List<Product> productList){
        Scanner userInput=new Scanner(System.in);
        displayProductViewMenu();
        int choice=userInput.nextInt();
        userInput.nextLine();
        if(choice==5 || choice==0){
            return;
        }
        handleProductShowList(choice,productList,userInput);

    }

    public static int chooseProductPage(){
        Scanner userInput=new Scanner(System.in);
        System.out.println("Enter the Product Id you want to select");
        System.out.println("0 for Exit");
        return userInput.nextInt();
    }

    public static void displayProductList(List<Product> productList){
        Scanner userInput=new Scanner(System.in);
        while (true){
            displayProductViewMenu();
            int choice=userInput.nextInt();
            userInput.nextLine();
            if(choice==5 || choice==0){
                break;
            }
            handleProductShowList(choice,productList,userInput);
        };

    }

    public static void handleProductShowList(int choice,List<Product> productList,Scanner userInput){
        switch (choice){
            case 1:{
                displayProducts(productList);
            }
            break;
            case 2:{
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                int option=userInput.nextInt();
                userInput.nextLine();
                if(option==1){
                    Collections.sort(productList, Comparator.comparingDouble(Product::getPrice));
                    displayProducts(productList);
                }else if(option==2){
                    Collections.sort(productList, Comparator.comparingDouble(Product::getPrice).reversed());
                    displayProducts(productList);
                }else{
                    System.out.println("Invalid Input");
                }

            }
            break;
            case 3:{
                System.out.println("Enter the Category Code or Name");
                String category=userInput.nextLine().toLowerCase();
                List<Product> filterlist=productList.stream()
                        .filter(product -> (product.getCategory().getName().toLowerCase().contains(category) || product.getCategory().getCode().toLowerCase().contains(category)))
                        .collect(Collectors.toList());
                displayProducts(filterlist);
            }
            break;
            case 4:{
                System.out.println("Enter the Keywords");
                String keyword=userInput.nextLine().toLowerCase();
                List<Product> filterlist=productList.stream()
                        .filter(product -> (String.valueOf(product.getId()).contains(keyword) || product.getCategory().getName().toLowerCase().contains(keyword) || product.getCategory().getCode().toLowerCase().contains(keyword)
                                || product.getName().toLowerCase().contains(keyword)  || String.valueOf(product.getPrice()).contains(keyword)  || String.valueOf(product.getQuantity()).contains(keyword)))
                        .collect(Collectors.toList());
                displayProducts(filterlist);
            }
            break;
            case 5:
                return;
            default:
                System.out.println();

        }
    }

    public static PurchaseDto addPurchaseItem(int currentUserId){
        Scanner userInput=new Scanner(System.in);
        PurchaseDto purchase=new PurchaseDto();

        System.out.println("Enter Purchased Date (yyyy-MM-dd HH:mm:ss): ");
        purchase.setPurchaseDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println("Enter Purchase Amount:");
        purchase.setAmount(userInput.nextDouble());

        userInput.nextLine();

        System.out.println("Enter Dealer Email: ");
        purchase.setDealerName(userInput.nextLine());

        System.out.println("Enter Dealer MobileNumber: ");
        purchase.setDealerContact(userInput.nextLine());
        List<PurchaseHistoryDto> purchaseList=new ArrayList<>();
        while (true){
            PurchaseHistoryDto detail=new PurchaseHistoryDto();

            System.out.println("Enter Product Id: ");
            detail.setProductId(userInput.nextInt());

            userInput.nextLine();

            System.out.println("Enter Quantity: ");
            detail.setQuantity(userInput.nextDouble());
            userInput.nextLine();

            System.out.println("Enter Amount: ");
            detail.setAmount(userInput.nextDouble());
            userInput.nextLine();

            System.out.println("Enter Sale Amount: ");
            detail.setSalePrice(userInput.nextDouble());
            userInput.nextLine();

            System.out.println("Enter Expiry Date (yyyy-MM-dd HH:mm:ss):");
            detail.setExpiryDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            detail.setExpired(false);

            purchaseList.add(detail);

            System.out.println("Do you want to continue to add product(y/n)?");
            String choice=userInput.nextLine();
            if(choice.toLowerCase().equals("y")){
                continue;
            }else{
                break;
            }
        }
        purchase.setPurchaseList(purchaseList);

        return purchase;
    }

}
