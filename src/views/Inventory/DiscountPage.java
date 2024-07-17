package views.Inventory;

import Extension.DiscountType;
import Models.Coupon;
import Models.Discount;
import Services.DiscountService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class DiscountPage {
    public static Discount addDiscount(Discount discount) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Choose Discount Type: ");
        System.out.println("1. "+ DiscountType.UPI.name());
        System.out.println("2. "+ DiscountType.CreditCard.name());
        System.out.println("3. "+ DiscountType.DebitCard.name());
        System.out.println("4. "+ DiscountType.Amount.name());
        discount.setDiscountType(userInput.nextLine());

        if(Integer.parseInt(discount.getDiscountType())<1 && Integer.parseInt(discount.getDiscountType())>4){
            System.out.println("Invalid DiscountType");
            return null;
        }

        System.out.println("Enter Percentage: ");
        discount.setPercentage(userInput.nextDouble());
        userInput.nextLine();
        System.out.println("Enter Description: ");
        discount.setDescription(userInput.nextLine());
        System.out.println("Enter Valid From Date (yyyy-MM-dd HH:mm:ss): ");
        discount.setValidFromDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Enter Valid To Date (yyyy-MM-dd HH:mm:ss): ");
        discount.setValidToDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return discount;
    }
    public static Discount updateDiscount(DiscountService discountService) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Discount ID to update: ");
        int discountId = userInput.nextInt();
        userInput.nextLine(); // Consume newline

        Discount discount = discountService.getDiscountDetails(discountId);

        if (discount == null) {
            System.out.println("Discount not found!");
            return null;
        }

//        System.out.println("Enter new Discount Type (current: " + discount.getDiscountType() + "): ");
//        discount.setDiscountType(userInput.nextLine());
        System.out.println("Enter new Percentage (current: " + discount.getPercentage() + "): ");
        discount.setPercentage(userInput.nextDouble());
        userInput.nextLine();
        System.out.println("Enter new Description (current: " + discount.getDescription() + "): ");
        discount.setDescription(userInput.nextLine());
        System.out.println("Enter new Valid From Date (yyyy-MM-dd HH:mm:ss) (current: " + discount.getValidFromDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "): ");
        discount.setValidFromDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("Enter new Valid To Date (yyyy-MM-dd HH:mm:ss) (current: " + discount.getValidToDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "): ");
        discount.setValidToDate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return discount;
    }
    public static int deleteDiscount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Discount Id: ");
        return scanner.nextInt();
    }

    public static void displayDiscountViewMenu() {
        System.out.println("1. Add Discount");
        System.out.println("2. Update Discount");
        System.out.println("3. View All Discounts");
        System.out.println("4. Exit");
    }

    public static void displayDiscounts(List<Discount> discountList) {
        System.out.println(String.format("%-15s  %-20s  %-10s %-25s %-20s %-20s", "DiscountId", "Type", "Percentage", "Description", "Valid From", "Valid To"));
        for (Discount discount : discountList) {
            System.out.println(discount.toString());
        }
    }

    public static Coupon addCoupon(Coupon couponDto) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter Coupon Code: ");
        couponDto.setCode(userInput.nextLine());

        System.out.println("Enter Value: ");
        couponDto.setValue(userInput.nextDouble());
        userInput.nextLine();

        System.out.println("Enter Min Amount Value: ");
        couponDto.setMinamount(userInput.nextDouble());
        userInput.nextLine();

        System.out.println("Enter Description: ");
        couponDto.setDescription(userInput.nextLine());

        System.out.println("Enter Valid From Date (yyyy-MM-dd HH:mm:ss): ");
        couponDto.setValidfromdate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println("Enter Valid To Date (yyyy-MM-dd HH:mm:ss): ");
        couponDto.setValidtodate(LocalDateTime.parse(userInput.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        couponDto.setUsed(false);
        couponDto.setAvailableclaims(0);
        System.out.println("Enter Total Claim Count: ");
        couponDto.setTotalclaims(userInput.nextInt());
        userInput.nextLine();
        return couponDto;
    }

    public static void displayCoupons(List<Coupon> couponList) {
        System.out.println(String.format("%-15s  %-20s  %-10s %-25s %-20s %-20s %-25s", "CouponId", "Coupon Code", "Value", "Description", "Valid From", "Valid To","Available Claims"));
        for (Coupon coupon : couponList) {
            System.out.println(String.format("%-15d %-20s  %-10.2f %-25s %-20s %-20s %-25d", coupon.getId(), coupon.getCode(),coupon.getValue(),coupon.getDescription(), coupon.getValidfromdate(), coupon.getValidtodate(),coupon.getAvailableclaims()));

        }
    }

}
