package views.Inventory;

import Models.ProductDiscount;
import Services.DiscountService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ProductDiscountPage {
    public static ProductDiscount addProductDiscount(ProductDiscount productDiscount) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Product Id: ");
        productDiscount.getProduct().setId(userInput.nextInt());
        userInput.nextLine();
        System.out.println("Enter Percentage: ");
        productDiscount.setPercentage(userInput.nextDouble());
        userInput.nextLine();
        System.out.println("Enter Description: ");
        productDiscount.setDescription(userInput.nextLine());
        System.out.println("Enter Valid From Date (yyyy-MM-dd HH:mm:ss): ");
        productDiscount.setValidFromDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Enter Valid To Date (yyyy-MM-dd HH:mm:ss): ");
        productDiscount.setValidToDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return productDiscount;
    }
    public static int deleteProductDiscount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Product Discount Id: ");
        return scanner.nextInt();
    }
    public static ProductDiscount updateProductDiscount(DiscountService productDiscountService) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Product Discount ID to update: ");
        int productDiscountId = userInput.nextInt();
        userInput.nextLine(); // Consume newline

        ProductDiscount productDiscount = productDiscountService.getProductDiscountDetails(productDiscountId);

        if (productDiscount == null) {
            System.out.println("Product Discount not found!");
            return null;
        }

        System.out.println("Enter new Product Id (current: " + productDiscount.getProduct().getId() + "): ");
        productDiscount.getProduct().setId(userInput.nextInt());
        userInput.nextLine(); // Consume newline
        System.out.println("Enter new Percentage (current: " + productDiscount.getPercentage() + "): ");
        productDiscount.setPercentage(userInput.nextDouble());
        userInput.nextLine(); // Consume newline
        System.out.println("Enter new Description (current: " + productDiscount.getDescription() + "): ");
        productDiscount.setDescription(userInput.nextLine());
        System.out.println("Enter new Valid From Date (yyyy-MM-dd HH:mm:ss) (current: " + productDiscount.getValidFromDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "): ");
        productDiscount.setValidFromDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Enter new Valid To Date (yyyy-MM-dd HH:mm:ss) (current: " + productDiscount.getValidToDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "): ");
        productDiscount.setValidToDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return productDiscount;
    }

    public static void displayProductDiscountViewMenu() {
        System.out.println("1. Add Discount");
        System.out.println("2. Update Discount");
        System.out.println("3. View All Discounts");
        System.out.println("0. Exit");
    }
    public static void displayProductPurchaseMenu() {
        System.out.println("1. Add Purchase");
        System.out.println("2. View PurchaseList");
        System.out.println("3. View Expiry Products");
        System.out.println("0. Exit");
    }

    public static void displayProductDiscounts(List<ProductDiscount> productDiscountList) {
        System.out.println(String.format("%-15s  %-10s  %-10s %-25s %-20s %-20s", "DiscountId", "ProductId", "Percentage", "Description", "Valid From", "Valid To"));
        for (ProductDiscount discount : productDiscountList) {
            System.out.println(discount.toString());
        }
    }

    public static void displayCouponMenu() {
        System.out.println("1. Add Coupon");
//        System.out.println("2. Update Coupon");
        System.out.println("2. View All Coupons");
        System.out.println("0. Exit");
    }


}
