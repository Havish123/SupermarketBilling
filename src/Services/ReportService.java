package Services;

import DTO.*;
import Extension.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    static Connection conn = DatabaseUtil.getConnection();

    public List<ProductReportDto> getTopSellingProducts(){
        List<ProductReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT o.ProductId, p.Name as ProductName,\n" +
                    "Sum(o.Quantity) as SaleQuantity FROM Products p \n" +
                    "join OrderDetails o on p.Id=o.ProductId\n" +
                    "group by o.ProductId,p.Name\n" +
                    "order by SaleQuantity desc;";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                System.out.println("Product ID | Name | Quantity");
                while (rs.next()) {
                    ProductReportDto report=new ProductReportDto();
                    report.setId(rs.getInt("ProductId"));
                    report.setName(rs.getString("ProductName"));
                    report.setQuantity(rs.getDouble("SaleQuantity"));

                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<CustomerReportDto> getTopTenCustomerList(){
        List<CustomerReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "select o.CustomerId,c.Name as CustomerName,c.MobileNumber, Sum(o.TotalAmount) as PurchaseAmount from Orders o join Customers c on o.CustomerId=c.Id\n" +
                    "Group by o.CustomerId,c.Name,c.MobileNumber Order by PurchaseAmount desc limit 10";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    CustomerReportDto report=new CustomerReportDto();
                    report.setCustomerId(rs.getInt("CustomerId"));
                    report.setName(rs.getString("CustomerName"));
                    report.setMobileNumber(rs.getString("MobileNumber"));
                    report.setAmount(rs.getDouble("PurchaseAmount"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<FrequentPurchaseDto> getFrequentlyBroughtItems(int customerId){
        List<FrequentPurchaseDto> reportDatas=new ArrayList<>();
        try {

            String query = "select od.productid,p.name as productname,Count(od.productid) as purchasecount from orderdetails od join orders o on od.orderid =o.id " +
                    "join products p on od.productid =p.id where o.customerid =? group by od.productid ,p.name order by Count(od.productid) desc limit 10;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,customerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    FrequentPurchaseDto report=new FrequentPurchaseDto();
                    report.setProductId(rs.getInt("productid"));
                    report.setProductName(rs.getString("productname"));
                    report.setPurchaseCount(rs.getInt("purchasecount"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<BillReportDto> getBillStatementReport(int customerId){
        List<BillReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "select o.id as orderId,o.customerid,c.name as customerName,o.discountamount,o.totalamount,o.finalamount,o.isfromwallet,o.walletamount from orders o " +
                    "join customers c on o.customerid =c.id where o.customerid =? order by o.Id desc limit 10;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,customerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    BillReportDto report=new BillReportDto();
                    report.setOrderId(rs.getInt("orderId"));
                    report.setCustomerId(rs.getInt("customerid"));
                    report.setCustomerName(rs.getString("customerName"));
                    report.setDiscountAmount(rs.getDouble("discountamount"));
                    report.setTotalAmount(rs.getDouble("totalamount"));
                    report.setFinalAmount(rs.getInt("finalamount"));
                    report.setFromWallet(rs.getBoolean("isfromwallet"));
                    report.setWalletAmount(rs.getDouble("walletamount"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public DiscountReportDto getDiscountReport(int customerId){
        DiscountReportDto report=null;
        try {

            String query = "select sum(o.discountamount) as overalldiscount,sum(o.totalamount) as overallamount,((sum(o.discountamount)/sum(o.totalamount))*100) as overallsavings  from orders o where customerid =?;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,customerId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    report=new DiscountReportDto();
                    report.setOverAllAmount(rs.getDouble("overallamount"));
                    report.setOverAllDiscount(rs.getDouble("overalldiscount"));
                    report.setOverallSavings(rs.getDouble("overallsavings"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
    public LoyaltyPointsReportDto showLoyaltyPointsData(int customerId){
        LoyaltyPointsReportDto report=null;
        try {

            String query = "select lph.customerid,lph.PointsEarned,lph.PointsSpent,lph.CreatedDate\n" +
                    "from loyaltypoints lph where lph.customerid =? ORDER by lph.CreatedDate DESC;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,customerId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    report=new LoyaltyPointsReportDto();
                    report.setPointsSpend(rs.getInt("PointsSpent"));
                    report.setCurrentPoints(rs.getInt("PointsEarned"));
                    report.setPointsEarned(report.getPointsEarned()+report.getPointsSpend());

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
    public WalletDto getWalletReport(int id) throws SQLException {
        try {
            WalletDto wallet = null;

            String query = "select w.id,w.customerid,w.amount,w.modifieddate,c.name as customername from wallets w join customers c on w.customerid=c.id where w.customerid= ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                wallet = new WalletDto();
                wallet.setCustomerName(rs.getString("customername"));
                wallet.setId(rs.getInt("id"));
                wallet.setCustomerId(rs.getInt("customerid"));
                wallet.setAmount(rs.getDouble("amount"));
                wallet.setLastmodifiedDate(rs.getTimestamp("modifieddate").toLocalDateTime());

            }
            return wallet;
        }
        catch (SQLException e){
            System.out.println("Error in getUser " +e.getMessage());
            throw e;
        }
    }
    public List<DayWiseSaleReport> getSalesReport(int currentUserId){
        List<DayWiseSaleReport> reportDatas=new ArrayList<>();
        try {

            String query = "select OrderDate::date, Sum(FinalAmount) as TotalSales From Orders o" +
                    " Group by OrderDate::date order by OrderDate::date desc limit 10;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    DayWiseSaleReport report=new DayWiseSaleReport();
                    report.setTotalAmount(rs.getDouble("TotalSales"));
                    report.setSaleDate(rs.getTimestamp("OrderDate").toLocalDateTime());
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<CustomerReportDto> showCustomerNotButInLastOneMonth(int currentUserId){
        List<CustomerReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT c.Id,c.name FROM customers c" +
                    " LEFT JOIN orders o ON o.customerid = c.id WHERE o.orderdate < (current_date - interval '1 month') OR o.orderdate IS null ORDER BY c.id;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    CustomerReportDto report=new CustomerReportDto();
                    report.setCustomerId(rs.getInt("Id"));
                    report.setName(rs.getString("name"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<CustomerReportDto> showCustomersNotBuyAfterJoin(int currentUserId){
        List<CustomerReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "select c.Id,c.Name as CustomerName,c.CreatedDate as JoinedDate " +
                    "From Customers c left join Orders o on c.Id=o.CustomerId " +
                    "where o.Id is null order by c.createddate;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    CustomerReportDto report=new CustomerReportDto();
                    report.setCustomerId(rs.getInt("Id"));
                    report.setName(rs.getString("CustomerName"));
                    report.setJoinedDate(rs.getTimestamp("JoinedDate").toLocalDateTime());
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<EmployeeSaleReport> showEmployeeSalesReport(int currentUserId){
        List<EmployeeSaleReport> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT e.id as empid,e.name as employee, COUNT(o.id) AS bill_count,Sum(o.totalamount) as billamount" +
                    " FROM employees e JOIN orders o on o.createdby = e.id " +
                    "where o.ordertype =2 GROUP BY e.id ,e.name ORDER BY bill_count desc;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    EmployeeSaleReport report=new EmployeeSaleReport();
                    report.setEmployeeId(rs.getInt("empid"));
                    report.setEmployeeName(rs.getString("employee"));
                    report.setBillCount(rs.getInt("bill_count"));
                    report.setBillAmount(rs.getDouble("billamount"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }


    public List<ProductReportDto> showProductsWtLowStock(int currentUserId){
        List<ProductReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT p.Id,p.Name as ProductName,COALESCE(SUM(pph.availablequantity), 0) as AvailableQuantity FROM Products p " +
                    "LEFT JOIN ProductPurchaseHistory pph ON p.Id = pph.ProductId WHERE pph.ExpiryDate > NOW() OR pph.ExpiryDate IS NULL " +
                    "GROUP BY p.Id, p.Name HAVING COALESCE(SUM(pph.availablequantity), 0) <= 10 ORDER BY AvailableQuantity;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ProductReportDto report=new ProductReportDto();
                    report.setId(rs.getInt("Id"));
                    report.setName(rs.getString("ProductName"));
                    report.setQuantity(rs.getDouble("AvailableQuantity"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<ProductReportDto> showProductsExpiredButInventory(int currentUserId){
        List<ProductReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT p.Id,p.Name as ProductName,pph.availablequantity as AvailableQuantity,pph.ExpiryDate " +
                    "FROM Products p JOIN ProductPurchaseHistory pph ON p.Id = pph.ProductId " +
                    "WHERE pph.ExpiryDate <= NOW() AND pph.Quantity > 0 ORDER BY p.Name;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ProductReportDto report=new ProductReportDto();
                    report.setId(rs.getInt("Id"));
                    report.setName(rs.getString("ProductName"));
                    report.setQuantity(rs.getDouble("availablequantity"));
                    report.setExpiryDate(rs.getTimestamp("ExpiryDate").toLocalDateTime());
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

    public List<ProductReportDto> showProductsNotSoldAtAll(int currentUserId){
        List<ProductReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT p.id,p.name FROM Products p " +
                    "LEFT JOIN OrderDetails od ON p.Id = od.ProductId WHERE od.ProductId IS NULL;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ProductReportDto report=new ProductReportDto();
                    report.setId(rs.getInt("id"));
                    report.setName(rs.getString("name"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }
    public List<ProductReportDto> showProductsOutofStock(int currentUserId){
        List<ProductReportDto> reportDatas=new ArrayList<>();
        try {

            String query = "SELECT p.Id,p.Name as ProductName,COALESCE(SUM(pph.availablequantity), 0) as AvailableQuantity " +
                    "FROM Products p LEFT JOIN ProductPurchaseHistory pph ON p.Id = pph.ProductId " +
                    "WHERE pph.ExpiryDate > NOW() OR pph.ExpiryDate IS null GROUP BY p.Id, p.Name " +
                    "HAVING COALESCE(SUM(pph.availablequantity), 0) = 0 ORDER BY p.Name;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ProductReportDto report=new ProductReportDto();
                    report.setId(rs.getInt("id"));
                    report.setName(rs.getString("ProductName"));
                    report.setQuantity(rs.getDouble("AvailableQuantity"));
                    reportDatas.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportDatas;
    }

}
