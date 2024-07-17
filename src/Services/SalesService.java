package Services;

import DTO.*;
import Extension.DatabaseUtil;
import Extension.OrderType;
import Extension.TransactionType;
import Models.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesService {
    static Connection conn = DatabaseUtil.getConnection();

    public int saveOrders(OrderDto order){
        int orderId=0;
        try {
            initializeLoyaltyPoints(order.customer.getId(),order.getCurrentUserId());
            String orderQuery = "INSERT INTO Orders (CustomerId, BillNumber, OrderType, IsPaymentSuccess, TotalAmount, FinalAmount, OrderDate, CreatedDate,isfromwallet,walletamount,discountamount,createdby,discountId,iscouponapplied,couponId,paymentreference) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(),?,?,?,?,?,?,?,?) RETURNING Id";
            int billNumber=generateBillNumber();
            try (PreparedStatement stmt = conn.prepareStatement(orderQuery)) {
                stmt.setInt(1, order.customer.getId());
                stmt.setInt(2, billNumber);
                stmt.setInt(3, order.orderType); // Assuming 2 is for in-store purchase
                stmt.setBoolean(4, true);
                stmt.setDouble(5, order.totalAmount);
                stmt.setDouble(6, order.getFinalamount());
                stmt.setBoolean(7,order.isFromWallet());
                stmt.setDouble(8,order.getWalletAmount());
                stmt.setDouble(9,order.getDiscountAmount());
                stmt.setInt(10,order.getCurrentUserId());
                stmt.setInt(11,order.getDiscountId());
                stmt.setBoolean(12,order.isCouponApplied());
                stmt.setInt(13,order.getCouponId());
                stmt.setString(14,order.getReferenceKey());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        addMultipleOrderDetails(orderId, order.orderdetails);

                        updateStock(order.orderdetails);

                        addLoyaltyPointsHistory(order.customer.getId(),order.totalAmount,orderId,order.getCurrentUserId());

                        Thread.sleep(3000);
                        System.out.println("Purchase successful.");
                        if(order.isFromWallet()){
                            System.out.println("Wallet Amount: "+order.getWalletAmount());
                        }
                        System.out.println("Discount Applied: "+order.getDiscountAmount());

                        System.out.println("Total amount: " + order.totalAmount);
                        System.out.println("Final amount: " + order.getFinalamount());

                        if(order.isCouponApplied()){
                            updateCouponData(order.getCouponId(),order.getAvailableCouponClaims());
                        }

                        if(order.isFromWallet()){
                            UserServices services=new UserServices();
                            WalletTopUpDto wallet=new WalletTopUpDto();
                            wallet.setTransactionType(TransactionType.Debit.getValue());
                            wallet.setUserId(order.customer.getId());
                            wallet.setWalletamount(order.getWalletAmount());
                            wallet.setBalanceAmount(order.getBalanceWallet());
                            wallet.setWalletId(order.getWalletId());
                            services.updateMoneyInWallet(wallet);
                        }
                    }
                }
            }

        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public OrderDto getOrderbyBillNumber(int billnumber,int userId){
        OrderDto order=null;
        try{

            String query = "SELECT id,billnumber,ordertype,totalamount,finalamount,orderdate FROM orders WHERE customerid = ? and billnumber=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, billnumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        order=new OrderDto();
                        order.setId(rs.getInt("id"));
                        order.setBillnumber(rs.getInt("billnumber"));
                        order.setOrderTypeString(OrderType.getNameByValue(rs.getInt("ordertype")));
                        order.totalAmount=rs.getDouble("finalamount");
                        order.setOrderdate(rs.getTimestamp("orderdate").toLocalDateTime());

                        order.orderdetails=getOrderDetailsbyOrderId(order.getId());

                        return order;
                    }else{
                        return null;
                    }
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Sales - Error while processing your request");
        }
        return order;
    }

    public List<OrderDetailDto> getOrderDetailsbyOrderId(int orderId){
        List<OrderDetailDto> orders=new ArrayList<>();
        try{
            String query = "select p.name as productname,o.productid,o.quantity,o.totalamount,o.discountamount,o.finalamount from orderdetails o join products p on o.productid=p.id where orderid=? ;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,orderId);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    OrderDetailDto order=new OrderDetailDto();
                    order.setProductName(rs.getString("productname"));
                    order.setProductId(rs.getInt("productid"));
                    order.setQuantity(rs.getDouble("quantity"));
                    order.setPrice(rs.getDouble("totalamount"));
                    order.setFinalprice(rs.getDouble("finalamount"));
                    order.setDiscount(rs.getDouble("discountamount"));
                    orders.add(order);
                }
            }
        }catch (SQLException e){
            System.out.println("Sales - Error in Connection.");
        }
        return orders;
    }

    public List<MyOrderDto> getOrdersByuserId(int currentUserId,int limit,int offset){
        List<MyOrderDto> orders=new ArrayList<>();
        try{
            String query = "select id as orderid,billnumber,ordertype,totalamount,walletamount,finalamount,orderdate,COUNT(*) OVER() as totalcount from orders where customerid= ? order by orderdate desc limit ? offset ? ;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,currentUserId);
                stmt.setInt(2,limit);
                stmt.setInt(3,limit*offset);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    MyOrderDto order=new MyOrderDto();
                    order.setOrderId(rs.getInt("orderid"));
                    order.setBillnumber(rs.getInt("billnumber"));
                    order.setOrderType(OrderType.getNameByValue(rs.getInt("ordertype")));
                    order.setTotalAmount(rs.getDouble("totalamount"));
                    order.setFinalAmount(rs.getDouble("finalamount"));
                    order.setWalletAmount(rs.getDouble("walletamount"));
                    order.setOrderdate(rs.getTimestamp("orderdate").toLocalDateTime());
                    order.setTotalCount(rs.getInt("totalcount"));
                    orders.add(order);
                }
            }
        }catch (SQLException e){
            System.out.println("Sales - Error in Connection.");
        }
        return orders;
    }


    private void addMultipleOrderDetails(int orderId, List<OrderDetailDto> orders) {
        try  {
            conn.setAutoCommit(false);
            String query = "INSERT INTO OrderDetails (OrderId, ProductId, Quantity, TotalAmount,discountamount,finalamount) VALUES (?, ?, ?, ?,?,?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (OrderDetailDto orderDetail : orders) {
                    stmt.setInt(1,orderId);
                    stmt.setInt(2, orderDetail.getProductId());
                    stmt.setDouble(3, orderDetail.getQuantity());
                    stmt.setDouble(4, orderDetail.getPrice());
                    stmt.setDouble(5, orderDetail.getDiscount());
                    stmt.setDouble(6, orderDetail.getFinalprice());
                    stmt.addBatch();
                }

                // Execute the batch
                stmt.executeBatch();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int generateBillNumber() {
        try  {
            String query = "select billnumber from orders o order by Id desc limit 1;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("billnumber")+1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public Product getProductPrice(int productId) {
        Product product=null;
        try  {
            String query = "SELECT Id,Name,CategoryId,Price FROM Products WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        product=new Product();
                        product.setPrice(rs.getDouble("Price"));
                        product.setName(rs.getString("Name"));
                        product.getCategory().setId(rs.getInt("CategoryId"));
                        product.setId(rs.getInt("Id"));
                        return product;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public Product getProductPriceNew(int productId, double quantity) {
        Product product = null;
        try {
            String query = "SELECT p.Id, p.Name, p.CategoryId, pp.SalePrice " +
                    "FROM Products p " +
                    "JOIN ProductPurchaseHistory pp ON p.Id = pp.ProductId " +
                    "WHERE p.Id = ? AND pp.ExpiryDate > CURRENT_TIMESTAMP AND pp.Quantity >= ? " +
                    "ORDER BY pp.ExpiryDate ASC " +
                    "LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);
                stmt.setDouble(2, quantity);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        product = new Product();
                        product.setPrice(rs.getDouble("SalePrice"));
                        product.setName(rs.getString("Name"));
                        product.getCategory().setId(rs.getInt("CategoryId"));
                        product.setId(rs.getInt("Id"));
                        return product;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public int getAvailableQuantityNew(int productId) throws SQLException {
        String query = "SELECT SUM(availablequantity) as totalQuantity FROM ProductPurchaseHistory WHERE ProductId = ? AND isexpired = false AND ExpiryDate > CURRENT_TIMESTAMP";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalQuantity");
                }
            }
        }
        return 0;
    }

    private void markAsSold(int purchaseHistoryId, int soldQuantity, int productId, LocalDateTime expiryDate) throws SQLException {
        String updateQuery = "UPDATE ProductPurchaseHistory SET availablequantity = 0, isexpired = true WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, purchaseHistoryId);
            stmt.executeUpdate();
        }
//        addSoldRecord(purchaseHistoryId, soldQuantity, productId, expiryDate);
    }

    private void processProductSale(int productId, double quantityToSell) throws SQLException {
        String query = "SELECT Id, availablequantity, ExpiryDate FROM ProductPurchaseHistory WHERE ProductId = ? AND isexpired = false AND ExpiryDate > CURRENT_TIMESTAMP ORDER BY ExpiryDate ASC";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next() && quantityToSell > 0) {
                    int purchaseHistoryId = rs.getInt("Id");
                    int availableStock = rs.getInt("availablequantity");
                    LocalDateTime expiryDate = rs.getTimestamp("ExpiryDate").toLocalDateTime();

                    if (availableStock > 0) {
                        if (expiryDate.isBefore(LocalDateTime.now())) {
                            markAsExpired(purchaseHistoryId);
                        } else {
                            if (quantityToSell >= availableStock) {
                                quantityToSell -= availableStock;
                                markAsSold(purchaseHistoryId, availableStock, productId, expiryDate);
                            } else {
                                String updateQuery = "UPDATE ProductPurchaseHistory SET availablequantity = ? WHERE Id = ?";
                                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                                    pstmt.setDouble(1, availableStock - quantityToSell);
                                    pstmt.setInt(2, purchaseHistoryId);
                                    pstmt.executeUpdate();
                                }
                                quantityToSell = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private void markAsExpired(int purchaseHistoryId) throws SQLException {
        String updateQuery = "UPDATE ProductPurchaseHistory SET isexpired = true WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, purchaseHistoryId);
            stmt.executeUpdate();
        }
    }

    private void updateStock(List<OrderDetailDto> orders) throws SQLException {
        try  {
            for (OrderDetailDto orderDetail : orders) {
                if (orderDetail.getQuantity() > 0) {
                    processProductSale(orderDetail.getProductId(), orderDetail.getQuantity());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    public void initializeLoyaltyPoints(int customerId, int userId) {
        try {
            String checkQuery = "SELECT Id FROM LoyaltyPoints WHERE CustomerId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, customerId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        String insertQuery = "INSERT INTO LoyaltyPoints (CustomerId, PointsEarned, PointsSpent, CreatedBy,createddate) VALUES (?, 0, 0, ?,now())";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, customerId);
                            insertStmt.setInt(2, userId);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLoyaltyPointsHistory(int customerId, double amount, int orderId, int userId) {
        try {
            conn.setAutoCommit(false);

            int pointsToAdd = (int) (amount / 100);
            if(pointsToAdd==0){
                return;
            }
            String updateLoyaltyPointsQuery = "UPDATE LoyaltyPoints SET PointsEarned = PointsEarned + ?, ModifiedDate = NOW(), ModifiedBy = ? WHERE CustomerId = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateLoyaltyPointsQuery)) {
                updateStmt.setInt(1, pointsToAdd);
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, customerId);
                updateStmt.executeUpdate();
            }

            int loyaltyPointsId = 0;
            String getLoyaltyPointsIdQuery = "SELECT Id FROM LoyaltyPoints WHERE CustomerId = ?";
            try (PreparedStatement getLoyaltyPointsIdStmt = conn.prepareStatement(getLoyaltyPointsIdQuery)) {
                getLoyaltyPointsIdStmt.setInt(1, customerId);
                try (ResultSet rs = getLoyaltyPointsIdStmt.executeQuery()) {
                    if (rs.next()) {
                        loyaltyPointsId = rs.getInt("Id");
                    }
                }
            }

            String insertHistoryQuery = "INSERT INTO LoyaltyPointHistory (PointsEarned, OrderId, loyaltyPointId, TransactionType, CreatedBy,createddate) VALUES (?, ?, ?, ?, ?,now())";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertHistoryQuery)) {
                insertStmt.setInt(1, pointsToAdd);
                insertStmt.setInt(2, orderId);
                insertStmt.setInt(3, loyaltyPointsId);
                insertStmt.setInt(4, 2);
                insertStmt.setInt(5, userId);
                insertStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    public void spendLoyaltyPoints(int customerId, int pointsToSpend, int orderId, int userId) {
        try {
            String selectQuery = "SELECT Id, pointsearned FROM LoyaltyPoints WHERE CustomerId = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, customerId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        int loyaltyPointId = rs.getInt("Id");
                        int totalPoints = rs.getInt("TotalPoints");

                        if (totalPoints >= pointsToSpend) {
                            String updateQuery = "UPDATE LoyaltyPoints SET PointsSpent = PointsSpent + ? WHERE Id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, pointsToSpend);
                                updateStmt.setInt(2, loyaltyPointId);
                                updateStmt.executeUpdate();
                            }

                            String insertHistoryQuery = "INSERT INTO LoyaltyPointHistory (PointsSpent, OrderId, LoyaltyPointId, TransactionType, CreatedBy) VALUES (?, ?, ?, 1, ?)";
                            try (PreparedStatement insertHistoryStmt = conn.prepareStatement(insertHistoryQuery)) {
                                insertHistoryStmt.setInt(1, pointsToSpend);
                                insertHistoryStmt.setInt(2, orderId);
                                insertHistoryStmt.setInt(3, loyaltyPointId);
                                insertHistoryStmt.setInt(4, userId);
                                insertHistoryStmt.executeUpdate();
                            }
                        } else {
                            System.out.println("Insufficient loyalty points");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCouponData(int couponId,int availableclaims) throws SQLException{
        String updateQuery = "UPDATE Coupon SET availableclaims = ?,isused=? WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            boolean isused=false;
            if(availableclaims-1==0){
                isused=true;
            }
            stmt.setInt(1, availableclaims-1);
            stmt.setBoolean(2, isused);
            stmt.setInt(3, couponId);
            stmt.executeUpdate();
        }
    }
}

