package views.Sales;

import Controller.CartController;
import Controller.PaymentController;
import DTO.*;
import Extension.OrderType;
import Extension.SecurityConfig;
import Interfaces.IPaymentService;
import Models.*;
import Services.CustomerService;
import Services.DiscountService;
import Services.SalesService;
import Services.UserServices;

import java.sql.SQLException;
import java.util.*;

public class BillingPage {
    public static OrderDto billProduct(int currentUserId){
        OrderDto order=new OrderDto();
        Scanner userInput = new Scanner(System.in);
        double totalAmount=0;
        List<OrderDetailDto> orders=new ArrayList<OrderDetailDto>();

        String input;
        try{
            while (true){
                OrderDetailDto detail=new OrderDetailDto();
                ShowBillProducts(orders);
                System.out.println("0 - Exit, * - for Bill, e-[ProductId] for Edit, r-[ProductId] for Remove");
                double totalprice=getTotalAmount(orders);
                double totalproductdiscount=getTotalDiscount(orders);
                System.out.println("Total Amount: "+totalprice);
                System.out.println("Discount For Products: "+totalproductdiscount);
                System.out.println("Overall Discount: "+getTotalDiscount(orders));
                System.out.println("Final Amount: "+String.valueOf((totalprice-totalproductdiscount)));

                System.out.println("Enter Product ID: ");
                input=userInput.nextLine();

                if(input.equals("0")){
                    break;
                }
                else if(input.equals("*")){
                    if(orders.size()>0){
                        break;
                    }else{
                        System.out.println("No Products Found");

                    }
                }
                else if(input.startsWith("e")){
                    int productId=input.split("-").length > 1 ? Integer.parseInt(input.split("-")[1]) : 0;
                    if(productId > 0){
                        EditProduct(orders,productId,userInput,0);
                        userInput.nextLine();
                    }else{
                        System.out.println("Invalid Product Id");
                    }
                }
                else if(input.startsWith("r")){
                    int productId=input.split("-").length > 1 ? Integer.parseInt(input.split("-")[1]) : 0;
                    if(productId > 0){
                        DeleteProduct(orders,productId);
                    }else{
                        System.out.println("Invalid Product Id");
                    }
                }
                else{
                    detail.setProductId(Integer.parseInt(input));

                    boolean isavailable=true;
                    while (true){
                        System.out.println("Enter Quantity(ProductId-"+detail.getProductId()+"): ");
                        detail.setQuantity(userInput.nextInt());
                        userInput.nextLine();

                        double availableQuantity=getAvailableQuantity(detail.getProductId());
                        if(availableQuantity==0){
                            isavailable=false;
                            System.out.println("Stocks not available.Please try again later.");
                            break;
                        }
                        else if(detail.getQuantity()>availableQuantity){
                            System.out.println("Insuffient Stock Available.");
                            System.out.println("Available Stocks : "+availableQuantity);

                        }else{
                            break;
                        }
                    }
                    if(!isavailable){
                        continue;
                    }
                    Product product = getProductPriceNew(detail.getProductId(),detail.getQuantity());
                    if (product==null) {
                        System.out.println("Invalid Product ID.");
                        continue;
                    }

                    if(isProductInList(orders,product.getId())){
                        System.out.println("Product Already Exists. Are you want to replace it?(Y/N)");
                        String choice=userInput.nextLine();
                        if(choice.toLowerCase().equals("y")){
                            EditProduct(orders,product.getId(),userInput,detail.getQuantity());
                        }

                    }else{
                        double totalproductamount=product.getPrice() * detail.getQuantity();

                        ProductDiscount discount=getProductDiscount(detail.getProductId());
                        if(discount!=null){
                            detail.setDiscount(calculateDiscountAmount(totalproductamount,discount.getPercentage()));
                            detail.setFinalprice(calculateFinalPrice(totalproductamount,detail.getDiscount()));
                        }

                        detail.setProductName(product.getName());
                        detail.setPrice(totalproductamount);

                        orders.add(detail);
                    }

                }

            }

            if(input.equals("*")){
//                BillProducts(orders,scanner);
                Customer customer=new Customer();
                while (true){
                    System.out.println("Enter the MobileNumber");
                    String mobilenumber=userInput.nextLine();
                    if(SecurityConfig.validateMobileNumber(mobilenumber)){
                        customer.setMobileNumber(mobilenumber);
                        break;
                    }else{
                        System.out.println("Invalid Mobile Number");
                    }
                }
                String customerName="";
                int billNumber=0;
                CustomerService customerservice=new CustomerService();

                int customerId=customerservice.getCustomerIdBasedOnMobilenumber(customer.getMobileNumber());

                double totalamount=getTotalAmount(orders);



                if(customerId==-1){
                    System.out.println("Enter Customer Name");
                    customer.setName(userInput.nextLine());
                    customerId=customerservice.createNewCustomer(customer);
                }else{
                    UserServices services=new UserServices();
                    WalletDto wallet=services.getWallet(customerId);
                    if(wallet!=null){
                        if(wallet.getAmount() > 0){
                            System.out.println("Your wallet Amount is : "+String.valueOf(wallet.getAmount()));
                            System.out.println("Can you spend your wallet amount here(y/n)?");
                            String waletinput=userInput.nextLine();
                            if(waletinput.toLowerCase().equals("y")){
                                if(wallet.getAmount()>(totalamount-order.getDiscountAmount())){
                                    order.setWalletAmount(wallet.getAmount()-(totalamount-order.getDiscountAmount()));
                                    order.setFromWallet(true);
                                    order.setFinalamount(0);
                                    order.setBalanceWallet(wallet.getAmount()-(totalamount-order.getDiscountAmount()));
                                }else
                                {
                                    order.setWalletAmount(wallet.getAmount());
                                    order.setFromWallet(true);
                                    order.setFinalamount((totalamount-order.getDiscountAmount())-wallet.getAmount());
                                    order.setBalanceWallet(0);
                                }
                                order.setWalletId(wallet.getId());
                            }else if(waletinput.toLowerCase().equals("n")){
                                order.setFromWallet(false);
                                order.setFinalamount(totalamount-order.getDiscountAmount());
                            }
                            else{
                                System.out.println("Invalid Input.");

                            }

                        }else{
                            order.setFromWallet(false);
                            order.setFinalamount(totalamount-order.getDiscountAmount());
                        }
                    }else{
                        order.setFromWallet(false);
                        order.setFinalamount(totalamount-order.getDiscountAmount());
                    }

                }
                Discount discount=getOverallDiscount(totalamount);
                if(discount!=null){
                    double overalldiscount = totalamount * (discount.getPercentage() / 100);
                    order.setDiscountId(discount.getId());
                    overalldiscount=overalldiscount+getTotalDiscount(orders);
                    order.setDiscountAmount(overalldiscount);
                    System.out.println("Discount Applied");
                    System.out.println(discount.getDescription());

                }else{
                    order.setDiscountAmount(getTotalDiscount(orders));
                }

                while(true){
                    System.out.println("Are you have Coupon Code? : If not hava please enter.");

                    String couponInput=userInput.nextLine();
                    if(!couponInput.isEmpty()){
                        Coupon coupon=getCouponData(couponInput,customerId);

                        if(coupon==null){
                            System.out.println("Invalid Coupon or Already Used.");
                        }else{
                            order.setCouponCode(couponInput);
                            order.setCouponApplied(true);
                            order.setCouponId(coupon.getId());
                            order.setAvailableCouponClaims(coupon.getAvailableclaims());
                            order.setDiscountAmount(order.getDiscountAmount()+coupon.getValue());
                            order.setCouponAmount(coupon.getValue());
                        }
                    }
                    break;
                }
                System.out.println("Total Amount :"+getTotalAmount(orders));

                if(order.isFromWallet()){
                    System.out.println("Wallet Amount Used :"+order.getWalletAmount());
                }
                if(order.isCouponApplied()){
                    System.out.println("Coupon Code Discount :"+order.getCouponAmount());
                }
                System.out.println("Total Discount Applied :"+order.getDiscountAmount());
                System.out.println("Total Amount to Be Paid :"+order.getFinalamount());

                if(order.isFromWallet()){
                    System.out.println("Balance Amount in Wallet :"+order.getBalanceWallet());
                }


                PaymentController paymentcontroller=new PaymentController();
                while(true){
                    System.out.println("Enter your Payment Option");
                    System.out.println("1. Cash ");
                    System.out.println("2. UPI ");
                    System.out.println("3. Debit Card");
                    System.out.println("4. Credit Card");
                    System.out.println("5. Net Banking");
                    int choice=userInput.nextInt();

                    userInput.nextLine();

                    String referenceKey=paymentcontroller.ProcessPayment(choice,order.getFinalamount());
                    if(referenceKey.isEmpty()){
                        System.out.println("Please Try Again");
                    }else{
                        order.setReferenceKey(referenceKey);
                        break;
                    }
                }


                customer.setId(customerId);

//                order.setDiscountAmount(getTotalDiscount(orders));
                order.orderdetails=orders;
                order.customer=customer;
                order.orderType= OrderType.Store.getValue();
                order.totalAmount=getTotalAmount(orders);
                order.setCurrentUserId(currentUserId);

//                SalesService service=new SalesService();
//                service.saveOrders(order);

            }else if(input.equals("0")){
                System.out.println("Close the current Billing.");
            }
        }catch (Exception e){
            System.out.println("Error"+e.getMessage());
        }

        return order;
    }
    public static double calculateDiscountAmount(double price, double discountPercentage) {
        return (price*discountPercentage)/Double.valueOf(100);
    }

    public static double calculateFinalPrice(double price, double discountAmount) {
        return price-discountAmount;
    }
    public static OrderDto checkoutProductByCartIds(UserDto user,Set<Integer> cartIds) throws SQLException{
        OrderDto order=new OrderDto();
        List<ProductCartDto> productIds=getProductIds(cartIds);
        List<OrderDetailDto> orders=new ArrayList<>();
        Scanner userInput = new Scanner(System.in);
        for(ProductCartDto productId:productIds){

            OrderDetailDto detail=new OrderDetailDto();
            detail.setCartId(productId.getCartId());
            detail.setProductId(productId.getProductId());

            while (true){
                System.out.println("Enter Quantity(ProductId-"+productId.getProductId()+"): ");
                detail.setQuantity(userInput.nextInt());
                userInput.nextLine();

                double availableQuantity=getAvailableQuantity(detail.getProductId());
                if(availableQuantity==0){
                    System.out.println("Stocks not available.Please try again later.");
                }
                else if(detail.getQuantity()>availableQuantity){
                    System.out.println("Insuffient Stock Available.");
                    System.out.println("Available Stocks : "+availableQuantity);

                }else{
                    break;
                }
            }

            Product product = getProductPriceNew(detail.getProductId(),detail.getQuantity());
            if (product==null) {
                System.out.println("Invalid Product ID.");
                continue;
            }
            if(isProductInList(orders,product.getId())){
                System.out.println("Product Already Exists. Are you want to replace it?(Y/N)");
                String choice=userInput.nextLine();
                if(choice.toLowerCase().equals("y")){
                    EditProduct(orders,product.getId(),userInput,detail.getQuantity());
                }
            }else{

                double totalproductamount=product.getPrice() * detail.getQuantity();

                ProductDiscount discount=getProductDiscount(detail.getProductId());
                if(discount!=null){
                    detail.setDiscount(calculateDiscountAmount(totalproductamount,discount.getPercentage()));
                    detail.setFinalprice(calculateFinalPrice(totalproductamount,detail.getDiscount()));
                }



                detail.setProductName(product.getName());
                detail.setPrice(totalproductamount);

                orders.add(detail);
            }
        }
        Customer customer=new Customer();
        customer.setMobileNumber(user.getMobileNumber());

        int customerId=user.getUserId();

        customer.setName(user.getName());
        customer.setId(customerId);

        UserServices services=new UserServices();

        WalletDto wallet=services.getWallet(user.getUserId());
        if(wallet!=null){
            if(wallet.getAmount() <getTotalAmount(orders)){
                System.out.println("Wallet have not enough money. Please topup your wallet.");
                return null;
            }
        }else{
            System.out.println("Error in Wallet. Please try again later.");
        }
        double totalamount=getTotalAmount(orders);

        Discount discount=getOverallDiscount(totalamount);
        if(discount!=null){
            double overalldiscount = totalamount * (discount.getPercentage() / 100);
            order.setDiscountId(discount.getId());
            overalldiscount=overalldiscount+getTotalDiscount(orders);
            order.setDiscountAmount(overalldiscount);
            System.out.println("Discount Applied");
            System.out.println(discount.getDescription());
        }
        while(true){
            System.out.println("Are you have Coupon Code? : If not hava please enter.");

            String couponInput=userInput.nextLine();
            if(!couponInput.isEmpty()){
                Coupon coupon=getCouponData(couponInput,customerId);

                if(coupon==null){
                    System.out.println("Invalid Coupon or Already Used.");
                }else{
                    order.setCouponCode(couponInput);
                    order.setCouponApplied(true);
                    order.setCouponId(coupon.getId());
                    order.setAvailableCouponClaims(coupon.getAvailableclaims());
                    order.setDiscountAmount(order.getDiscountAmount()+coupon.getValue());

                }
            }
            break;
        }
        if(wallet.getAmount()>(totalamount-order.getDiscountAmount())){
            order.setWalletAmount(wallet.getAmount()-(totalamount-order.getDiscountAmount()));
            order.setFromWallet(true);
            order.setFinalamount(0);
            order.setBalanceWallet(wallet.getAmount()-(totalamount-order.getDiscountAmount()));
        }else
        {
            order.setWalletAmount(wallet.getAmount());
            order.setFromWallet(true);
            order.setFinalamount((totalamount-order.getDiscountAmount())-wallet.getAmount());
            order.setBalanceWallet(0);
        }
        order.setWalletId(wallet.getId());

//        order.setDiscountAmount(getTotalDiscount(orders));
        order.orderdetails=orders;
        order.customer=customer;
        order.orderType= OrderType.Online.getValue();
        order.totalAmount=getTotalAmount(orders);
        order.setCurrentUserId(user.getUserId());


        return order;
    }

    public static OrderDto checkoutProduct(UserDto user, Set<Integer> productIds) throws SQLException {
        OrderDto order=new OrderDto();

        List<OrderDetailDto> orders=new ArrayList<>();
        Scanner userInput = new Scanner(System.in);
        for(int productid:productIds){

            OrderDetailDto detail=new OrderDetailDto();

            detail.setProductId(productid);

            while (true){
                System.out.println("Enter Quantity(ProductId-"+productid+"): ");
                detail.setQuantity(userInput.nextInt());
                userInput.nextLine();

                double availableQuantity=getAvailableQuantity(detail.getProductId());
                if(availableQuantity==0){
                    System.out.println("Stocks not available.Please try again later.");
                }
                else if(detail.getQuantity()>availableQuantity){
                    System.out.println("Insuffient Stock Available.");
                    System.out.println("Available Stocks : "+availableQuantity);

                }else{
                    break;
                }
            }
            Product product = getProductPriceNew(detail.getProductId(),detail.getQuantity());
            if (product==null) {
                System.out.println("Invalid Product ID.");
                continue;
            }

            if(isProductInList(orders,product.getId())){
                System.out.println("Product Already Exists. Are you want to replace it?(Y/N)");
                String choice=userInput.nextLine();
                if(choice.toLowerCase().equals("y")){
                    EditProduct(orders,product.getId(),userInput,detail.getQuantity());
                }
            }else{
                double totalproductamount=product.getPrice() * detail.getQuantity();

                ProductDiscount discount=getProductDiscount(detail.getProductId());
                if(discount!=null){
                    detail.setDiscount(calculateDiscountAmount(totalproductamount,discount.getPercentage()));
                    detail.setFinalprice(calculateFinalPrice(totalproductamount,detail.getDiscount()));
                }

                detail.setProductName(product.getName());
                detail.setPrice(totalproductamount);

                orders.add(detail);
            }
        }
        Customer customer=new Customer();
        customer.setMobileNumber(user.getMobileNumber());

        int customerId=user.getUserId();

        customer.setName(user.getName());
        customer.setId(customerId);
        UserServices services=new UserServices();

        WalletDto wallet=services.getWallet(user.getUserId());
        if(wallet!=null){
            if(wallet.getAmount() <getTotalAmount(orders)){
                System.out.println("Wallet have not enough money. Please topup your wallet.");
                return null;
            }
        }else{
            System.out.println("Error in Wallet. Please try again later.");
        }
        double totalamount=getTotalAmount(orders);

        Discount discount=getOverallDiscount(totalamount);
        if(discount!=null){
            double overalldiscount = totalamount * (discount.getPercentage() / 100);
            order.setDiscountId(discount.getId());
            overalldiscount=overalldiscount+getTotalDiscount(orders);
            order.setDiscountAmount(overalldiscount);
            System.out.println("Discount Applied");
            System.out.println(discount.getDescription());
        }else{
            order.setDiscountAmount(getTotalDiscount(orders));
        }

        while(true){
            System.out.println("Are you have Coupon Code? : If not hava please enter.");

            String couponInput=userInput.nextLine();
            if(!couponInput.isEmpty()){
                Coupon coupon=getCouponData(couponInput,customerId);

                if(coupon==null){
                    System.out.println("Invalid Coupon or Already Used.");
                }else{
                    order.setCouponCode(couponInput);
                    order.setCouponApplied(true);
                    order.setCouponId(coupon.getId());
                    order.setAvailableCouponClaims(coupon.getAvailableclaims());
                    order.setDiscountAmount(order.getDiscountAmount()+coupon.getValue());

                }
            }
            break;
        }
        if(wallet.getAmount()>(totalamount-order.getDiscountAmount())){
            order.setWalletAmount((totalamount-order.getDiscountAmount()));
            order.setFromWallet(true);
            order.setFinalamount(0);
            order.setBalanceWallet(wallet.getAmount()-(totalamount-order.getDiscountAmount()));
        }else
        {
            order.setWalletAmount(wallet.getAmount());
            order.setFromWallet(true);
            order.setFinalamount((totalamount-order.getDiscountAmount())-wallet.getAmount());
            order.setBalanceWallet(0);
        }
        order.setWalletId(wallet.getId());
        order.orderdetails=orders;
        order.customer=customer;
        order.orderType= OrderType.Online.getValue();
        order.totalAmount=totalamount;
        order.setCurrentUserId(user.getUserId());
        System.out.println("Process your order.");
        return order;
    }

    private static double getTotalAmount(List<OrderDetailDto> input){
        double sum = 0.0;
        for (OrderDetailDto order : input) {
            sum += order.getPrice();
        }
        return sum;
    }

    private static double getTotalDiscount(List<OrderDetailDto> input){
        double sum = 0.0;
        for (OrderDetailDto order : input) {
            sum += order.getDiscount();
        }
        return sum;
    }

    private static ProductDiscount getProductDiscount(int productId) {
        ProductDiscount product=null;
        DiscountService discountService=new DiscountService();

        product=discountService.getDiscountsForProducts(productId);
        discountService=null;
        return product;
    }

    private static Discount getOverallDiscount(double amount) {
        Discount discount=null;
        DiscountService discountService=new DiscountService();

        discount=discountService.getDiscountForPrice(amount);

        discountService=null;
        return discount;
    }

    private static Coupon getCouponData(String couponCode,int customerId) {
        Coupon discount=null;
        DiscountService discountService=new DiscountService();

        discount=discountService.getCouponData(couponCode);
        if(discount!=null){
            Boolean isused=discountService.hasUserUsedCoupon(customerId,discount.getId());
            if(isused){
                System.out.println("Already use the coupon code");
                discount=null;
            }
        }

        discountService=null;
        return discount;
    }

    private static Product getProductPrice(int productId) {
        Product product=null;
        SalesService saleService=new SalesService();

        product=saleService.getProductPrice(productId);
        saleService=null;
        return product;
    }
    private static Product getProductPriceNew(int productId,double quantity) {
        Product product=null;
        SalesService saleService=new SalesService();

        product=saleService.getProductPriceNew(productId,quantity);
        saleService=null;
        return product;
    }
    private static double getAvailableQuantity(int productId) throws SQLException {
        double quantity=0;
        SalesService saleService=new SalesService();

        quantity=saleService.getAvailableQuantityNew(productId);
        saleService=null;
        return quantity;
    }
    private static WalletDto getWalletAmount(int currentUserId) {
        try{
            WalletDto wallet=null;
            UserServices userService=new UserServices();

            wallet=userService.getWallet(currentUserId);
            userService=null;
            return wallet;
        }catch (Exception e){
            System.out.println("Error in Wallet.");
        }
       return null;
    }

    private static List<ProductCartDto> getProductIds(Set<Integer> cartIds) {
        CartController cartcontroller=new CartController();
        return cartcontroller.getProductIdsbyCartId(cartIds);
    }

    private static void ShowBillProducts(List<OrderDetailDto> orders){
        if(orders.size()>0){
            System.out.printf("%-15s %-15s %-10s %-10s %-10s%n", "ProductId", "Name", "Qty", "Discount","Price");
            System.out.println("----------------------------------------");
            for (OrderDetailDto product : orders) {
                System.out.printf("%-15d %-15s %-10.2f %-10.2f %-10.2f%n",
                        product.getProductId(),
                        product.getProductName(),
                        product.getQuantity(),
                        product.getDiscount(),
                        product.getPrice());
            }
        }

    }

    private static void EditProduct(List<OrderDetailDto> input,int ProductId,Scanner scanner,double quantity){
        for (OrderDetailDto order : input) {
            if (order.getProductId() == ProductId) {
                if(quantity<=0){
                    System.out.println("Enter the Quantity");
                    quantity=scanner.nextDouble();
                }

                Product product=getProductPriceNew(ProductId,quantity);
                double totalamount=quantity*product.getPrice();
                order.setPrice(totalamount);
                ProductDiscount discount=getProductDiscount(ProductId);
                if(discount!=null){
                    order.setDiscount(calculateDiscountAmount(totalamount,discount.getPercentage()));
                    order.setFinalprice(calculateFinalPrice(totalamount,order.getDiscount()));
                }
                order.setQuantity(quantity);
                System.out.println("Product Edited");
                break;
            }
        }

    }

    private static boolean isProductInList(List<OrderDetailDto> input, int productId) {
        for (OrderDetailDto order : input) {
            if (order.getProductId() == productId) {
                return true;
            }
        }
        return false;
    }


    private static void DeleteProduct(List<OrderDetailDto> input,int ProductId){
        OrderDetailDto removeOrder=null;
        for (OrderDetailDto order : input) {
            if (order.getProductId() == ProductId) {
                removeOrder = order;
                break;
            }
        }
        if (removeOrder != null) {
            input.remove(removeOrder);
        }
    }

    public static int displayProductDetails(Product product){
        try{
            Scanner userInput=new Scanner(System.in);
            System.out.println("Product Details");
            System.out.println("Product Name: "+product.getName());
            System.out.println("Product Category: "+ product.getCategory().getName());
            System.out.println("Product Price: "+ product.getPrice());

            if(product.getQuantity()==0){
                System.out.println("Product is out of stock");
            }else{
                if(product.getQuantity() < 4)
                    System.out.println("Only "+ product.getQuantity() + " left. Hurry !!");
                else
                    System.out.println("Product is available");
            }

            System.out.println("-----------**********-----------");
            System.out.println("Enter 1: to add this product to cart");
            System.out.println("Enter 2: to Buy this product now");
            System.out.println("Enter 0: Exit");

            int choice=userInput.nextInt();
            userInput.nextLine();
            return choice;
        }catch (Exception e){
            System.out.println("Error while processing your request");
        }
        return 0;
    }

    public static int billdetails(){
        Scanner scanner=new Scanner(System.in);
        int billnumber=0;
        try{
            System.out.println("Enter the billnumber you want to see. 0 - for exit");
            billnumber=scanner.nextInt();

        }catch (InputMismatchException e){
            System.out.println("Invalid Input");

        }
        return billnumber;
    }

    public static void showBillDetails(OrderDto order){
        System.out.println("Bill Details");
        System.out.println("Bill Number: "+ order.getBillnumber());
        System.out.println("Order Date: "+order.getLocalDateString() );
        System.out.println("Total Amount: "+order.totalAmount);

        System.out.println(String.format("%-15s %-23s %-12s %-12s","ProductId","ProductName","Quantity","Amount"));
        for(OrderDetailDto ordetails:order.orderdetails){
            System.out.println(ordetails.toString());
        }
        System.out.println();
    }
}
