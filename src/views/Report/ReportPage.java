package views.Report;

import DTO.*;

import java.util.List;

public class ReportPage {

    public static void showTop10Customers(List<CustomerReportDto> reportData){
        System.out.println(String.format("%-15s %-23s %-15s %-11s","CustomerId","Name","MobileNumber","Amount"));
        for(CustomerReportDto report:reportData){
            System.out.println(report.toString());
        }
    }

    public static void showTopSellingProducts(List<ProductReportDto> reportData){
        System.out.println(String.format("%-15s %-23s %-11s","ProductId","Name","Quantity"));
        for(ProductReportDto report:reportData){
            System.out.println(report.toString());
        }
    }

    public static void showFrequentlyBroghtItemsByCustomer(List<FrequentPurchaseDto> purchaseList){
        System.out.println(String.format("%-15s %-23s %-15s","ProductId","Name","PurchaseCount"));
        for(FrequentPurchaseDto purchase:purchaseList){
            System.out.println(purchase.toString());
        }
    }

    public static void showBillStatements(List<BillReportDto> purchaseList){
        System.out.println(String.format("%-15s %-15s %-20s %-15s %-20s %-20s %-20s","OrderId","Bill Number","CustomerName","Discount","Total Amount","Final Amount","WalletAmount"));
        for(BillReportDto purchase:purchaseList){
            System.out.println(purchase.toString());
        }
    }

    public static void showDiscountReport(DiscountReportDto report){
        System.out.println("Discount Report");
        System.out.println("Overall Purchase : "+report.getOverAllAmount());
        System.out.println("Overall Discount : "+report.getOverAllDiscount());
        System.out.println("Overall Savings(%) : "+report.getOverallSavings());
    }
    public static void showLoyaltyPointsData(LoyaltyPointsReportDto report){
        System.out.println("LoyaltyPoints Report");
        System.out.println("Points Earned : "+report.getPointsEarned());
        System.out.println("Points Spent : "+report.getPointsSpend());
        System.out.println("Current Points : "+report.getCurrentPoints());
    }

    public static void showWalletData(WalletDto report){
        if(report==null){
            System.out.println("Wallet Account not created, Please purchase product.");
        }else{
            System.out.println("Wallet Details");
            System.out.println("Customer Id : "+report.getCustomerId());
            System.out.println("Name : "+report.getCustomerName());
            System.out.println("Wallet Amount : "+report.getAmount());
            System.out.println("Last Transaction Date : "+report.getLocalDateString());
        }

    }


    public static void showDaywiseRevenue(List<DayWiseSaleReport> salesReport){
        System.out.println("Day Wise Revenue");

        System.out.println(String.format("%-25s %-25s","Date","Total Amount of Sales"));
        for(DayWiseSaleReport report:salesReport){
            System.out.println(report.toString());
        }
    }

    public static void SalesPersonReport(List<EmployeeSaleReport> reports){

        System.out.println(String.format("%-15s %-25s %-15s %-13s","EmployeeId","EmployeeName","Bill Count","Bill Amount"));

        for(EmployeeSaleReport report:reports){
            System.out.println(report.toString());
        }
    }

    public static void showCustomerNotButInLastOneMonth(List<CustomerReportDto> reportDatas){
        System.out.println("Customer who didn't buy Products in last one month");

        System.out.println(String.format("%-15s %-25s","CustomerId","CustomerName"));
        for(CustomerReportDto customer:reportDatas){
            System.out.println(String.format("%-15d %-25s",customer.getCustomerId(),customer.getName()));
        }
    }
    public static void showCustomersNotBuyAfterJoin(List<CustomerReportDto> reportDatas){
        System.out.println("Customer who didn't buy Products");

        System.out.println(String.format("%-15s %-25s %-25s","CustomerId","CustomerName","Joined Date"));
        for(CustomerReportDto customer:reportDatas){
            System.out.println(String.format("%-15d %-25s %-25s",customer.getCustomerId(),customer.getName(),customer.getLocalDateString()));
        }
    }

    public static void showProductsWtLowStock(List<ProductReportDto> reportDatas){
        System.out.println("Products with Low Stock");

        System.out.println(String.format("%-15s %-25s %-25s","ProductId","ProductName","Quantity"));
        for(ProductReportDto product:reportDatas){
            System.out.println(String.format("%-15d %-25s %-12.2f",product.getId(),product.getName(),product.getQuantity()));
        }
    }
    public static void showProductsExpiredButInventory(List<ProductReportDto> reportDatas){
        if(reportDatas.size()>0){
            System.out.println("Products Expired but in Inventory");

            System.out.println(String.format("%-15s %-25s %-25s %-25s","ProductId","ProductName","Quantity","Expiry Date"));
            for(ProductReportDto product:reportDatas){
                System.out.println(String.format("%-15d %-25s %-12.2f %-25s",product.getId(),product.getName(),product.getQuantity(),product.getLocalDateString()));
            }
        }else{
            System.out.println("No Record Found");
        }

    }

    public static void showProductsNotSoldAtAll(List<ProductReportDto> reportDatas){
        System.out.println("Products Not Sold At all");

        System.out.println(String.format("%-15s %-25s","ProductId","ProductName"));
        for(ProductReportDto product:reportDatas){
            System.out.println(String.format("%-15d %-25s",product.getId(),product.getName()));
        }
    }

    public static void showProductsOutofStock(List<ProductReportDto> reportDatas){
        System.out.println("Products with Out of Stock");

        System.out.println(String.format("%-15s %-25s %-25s","ProductId","ProductName","Quantity"));
        for(ProductReportDto product:reportDatas){
            System.out.println(String.format("%-15d %-25s %-12.2f",product.getId(),product.getName(),product.getQuantity()));
        }
    }
}
