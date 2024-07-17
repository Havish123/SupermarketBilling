package Controller;

import DTO.*;
import Services.ReportService;
import Services.UserServices;
import views.Report.ReportPage;

import java.util.List;

public class ReportController {

    ReportService reportService = new ReportService();


    public void showTopSellingProducts(){
        try{
            List<ProductReportDto> reportdata=reportService.getTopSellingProducts();
            ReportPage.showTopSellingProducts(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showTopTenCustomerList(){
        try{
            List<CustomerReportDto> reportdata=reportService.getTopTenCustomerList();
            ReportPage.showTop10Customers(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showFrequentBroughtItems(int customerId){
        try{
            List<FrequentPurchaseDto> reportdata=reportService.getFrequentlyBroughtItems(customerId);
            ReportPage.showFrequentlyBroghtItemsByCustomer(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showBillStatementReport(int customerId){
        try{
            List<BillReportDto> reportdata=reportService.getBillStatementReport(customerId);
            ReportPage.showBillStatements(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showDiscountReport(int customerId){
        try{
            DiscountReportDto reportdata=reportService.getDiscountReport(customerId);
            ReportPage.showDiscountReport(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showLoyaltyPointsData(int customerId){
        try{
            LoyaltyPointsReportDto reportdata=reportService.showLoyaltyPointsData(customerId);
            ReportPage.showLoyaltyPointsData(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showWalletData(int customerId){
        try{
            WalletDto reportdata=reportService.getWalletReport(customerId);
            ReportPage.showWalletData(reportdata);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showSaleReport(int currentUserId){
        try{
            List<DayWiseSaleReport> salesReport=reportService.getSalesReport(currentUserId);
            ReportPage.showDaywiseRevenue(salesReport);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showCustomerDidntButLastMonth(int currentUserId){
        try{
            List<CustomerReportDto> reports=reportService.showCustomerNotButInLastOneMonth(currentUserId);

            ReportPage.showCustomerNotButInLastOneMonth(reports);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showEmployeeSaleReport(int currentUserId){
        try{
            List<EmployeeSaleReport> reports=reportService.showEmployeeSalesReport(currentUserId);

            ReportPage.SalesPersonReport(reports);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showCustomersNotBuyAfterJoin(int currentUserId){
        try{
            List<CustomerReportDto> report=reportService.showCustomersNotBuyAfterJoin(currentUserId);
            ReportPage.showCustomersNotBuyAfterJoin(report);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void showProductsWtLowStock(int currentUserId){
        try{
            List<ProductReportDto> report=reportService.showProductsWtLowStock(currentUserId);
            ReportPage.showProductsWtLowStock(report);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showProductsExpiredButInventory(int currentUserId){
        try{
            List<ProductReportDto> report=reportService.showProductsExpiredButInventory(currentUserId);
            ReportPage.showProductsExpiredButInventory(report);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showProductsNotSoldAtAll(int currentUserId){
        try{
            List<ProductReportDto> report=reportService.showProductsNotSoldAtAll(currentUserId);
            ReportPage.showProductsNotSoldAtAll(report);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void showProductsOutofStock(int currentUserId){
        try{
            List<ProductReportDto> report=reportService.showProductsOutofStock(currentUserId);
            ReportPage.showProductsOutofStock(report);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
