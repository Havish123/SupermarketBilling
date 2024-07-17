package Services;

import DTO.PurchaseDto;
import DTO.PurchaseHistoryDto;
import Extension.DatabaseUtil;
import Models.Category;
import Models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    static Connection conn = DatabaseUtil.getConnection();

    public int addProduct(Product product){
        try {
            String query = "INSERT INTO Products (Name, CategoryId, Price,isactive) VALUES (?, ?, ?,true)";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, product.getName());
                stmt.setInt(2, product.getCategory().getId());
                stmt.setDouble(3, product.getPrice());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int productId = generatedKeys.getInt(1);
                        }
                    }
                }
                return  rowsInserted;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateProduct(Product product,int currentUserId){
        try{
            StringBuilder query = new StringBuilder("UPDATE Products SET name=? ,price=? where id=?");

            try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
                stmt.setString(1,product.getName());
                stmt.setDouble(2,product.getPrice());
                stmt.setInt(3,product.getId());
                int rowsUpdated=stmt.executeUpdate();

                return rowsUpdated;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteProduct(int ProductId){
        try{
            StringBuilder query = new StringBuilder("Update Products set isactive=false where id=?");

            try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
                stmt.setInt(1,ProductId);
                int rowsUpdated=stmt.executeUpdate();

                return rowsUpdated;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public int deleteCategory(int categoryId){
        try{
            StringBuilder query = new StringBuilder("Update Category set isactive=false where id=?");

            try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
                stmt.setInt(1,categoryId);
                int rowsUpdated=stmt.executeUpdate();

                return rowsUpdated;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public int addCategory(Category category){
        try {
            String query = "INSERT INTO Category (Name, Code,isactive) VALUES (?, ?,true)";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, category.getName());
                stmt.setString(2, category.getCode());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {

                    System.out.println("Category added successfully.");
                }
                return  rowsInserted;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Category> getCategoryList(){
        List<Category> categoryList=new ArrayList<>();
        try {
            String query = "SELECT id,name,code,isactive FROM Category where isactive=true;";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Category category=new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));
                    category.setCode(rs.getString("code"));
                    category.setActive(rs.getBoolean("isactive"));
                    categoryList.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public Product getProductById(int productId){
        Product product=new Product();
        try {
            String query = "SELECT p.id,p.name,c.code,c.name AS CategoryName,pph.saleprice AS price,SUM(pph.availablequantity) AS availablequantity,p.isactive " +
                    "FROM products p JOIN category c ON p.categoryid = c.id " +
                    "JOIN productpurchasehistory pph ON p.id = pph.productid JOIN productpurchase pp ON pp.id = pph.purchaseid " +
                    "WHERE p.isactive = true AND p.id = ? GROUP BY p.id, p.name, c.code, c.name,pph.saleprice, p.isactive;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    product=new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.getCategory().setCode(rs.getString("code"));
                    product.getCategory().setName(rs.getString("CategoryName"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getDouble("availablequantity"));
                    product.setActive(rs.getBoolean("isactive"));
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    public List<Product> getProductList(int categoryId){
        List<Product> productList=new ArrayList<>();
        try {
            String query="SELECT p.id,p.name,c.code,c.name AS CategoryName," +
                    "COALESCE(SUM(pph.availablequantity), 0) AS availablequantity,p.isactive,COALESCE(pph.saleprice, 0) as price,pph.expirydate " +
                    "FROM products p " +
                    "JOIN category c ON p.categoryid = c.id " +
                    "LEFT JOIN productpurchasehistory pph ON p.id = pph.productid " +
                    "WHERE pph.ExpiryDate > CURRENT_TIMESTAMP and p.isactive = true and ((? <> 0 and p.categoryid= ? ) or ? = 0) " +
                    "GROUP BY p.id, p.name, c.code, c.name, p.price, p.isactive,pph.saleprice,pph.expirydate ;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, categoryId);
                    stmt.setInt(2, categoryId);
                    stmt.setInt(3, categoryId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Product product=new Product();
                        product.setId(rs.getInt("id"));
                        product.setName(rs.getString("name"));
                        product.getCategory().setCode(rs.getString("code"));
                        product.getCategory().setName(rs.getString("CategoryName"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantity(rs.getDouble("availablequantity"));
                        product.setActive(rs.getBoolean("isactive"));
                        if(rs.getTimestamp("expirydate")!=null){
                            product.setExpiryDate(rs.getTimestamp("expirydate").toLocalDateTime());
                        }


                        productList.add(product);
                    }
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }


    public int addPurchaseData(PurchaseDto inputDto){
        try{
            String purchaseQuery = "INSERT INTO ProductPurchase (PurchaseDate, Amount, PurchasedBy, DealerEmail, DealerMobileNumber, CreatedDate) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

            try (PreparedStatement purchaseStmt = conn.prepareStatement(purchaseQuery, Statement.RETURN_GENERATED_KEYS)) {
                purchaseStmt.setTimestamp(1, Timestamp.valueOf(inputDto.getPurchaseDate()));
                purchaseStmt.setDouble(2, inputDto.getAmount());
                purchaseStmt.setInt(3, inputDto.getPurchasedBy());
                purchaseStmt.setString(4, inputDto.getDealerName());
                purchaseStmt.setString(5, inputDto.getDealerContact());

                int rowsInserted = purchaseStmt.executeUpdate();

                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = purchaseStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int purchaseId = generatedKeys.getInt(1);
                            inputDto.setId(purchaseId);
                            addPurchaseItems(inputDto);
                        }
                    }
                }else{
                    return 0;
                }

            }
            return inputDto.getId();
        }catch (Exception e){
                System.out.println("Error in Purchase");
        }
        return 0;
    }

    private void addPurchaseItems(PurchaseDto inputDto) throws SQLException{

        String purchaseHistoryQuery = "INSERT INTO ProductPurchaseHistory (PurchaseId, ProductId, Quantity, Amount, ExpiryDate, isexpired,saleprice,availablequantity) VALUES (?, ?, ?, ?, ?, ?,?,?)";

        for (PurchaseHistoryDto history : inputDto.getPurchaseList()) {
            try (PreparedStatement historyStmt = conn.prepareStatement(purchaseHistoryQuery)) {
                historyStmt.setInt(1, inputDto.getId());
                historyStmt.setInt(2, history.getProductId());
                historyStmt.setDouble(3, history.getQuantity());
                historyStmt.setDouble(4, history.getAmount());
                historyStmt.setTimestamp(5, Timestamp.valueOf(history.getExpiryDate()));
                historyStmt.setBoolean(6, history.isExpired());
                historyStmt.setDouble(7, history.getSalePrice());
                historyStmt.setDouble(8, history.getQuantity());
                historyStmt.executeUpdate();
            }
        }

    }

    public List<PurchaseDto> getPurchaseList(){

        String query = "SELECT * FROM ProductPurchase";
        List<PurchaseDto> purchases = new ArrayList<>();
        try{
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    PurchaseDto purchase = new PurchaseDto();
                    purchase.setId(rs.getInt("Id"));
                    purchase.setPurchaseDate(rs.getTimestamp("PurchaseDate").toLocalDateTime());
                    purchase.setAmount(rs.getDouble("Amount"));
                    purchase.setPurchasedBy(rs.getInt("PurchasedBy"));
                    purchase.setDealerName(rs.getString("DealerEmail"));
                    purchase.setDealerContact(rs.getString("DealerMobileNumber"));

                    purchases.add(purchase);
                }
            }
        }catch (Exception e){
            System.out.println("Error in Purchase List");
        }
        return purchases;
    }

    public PurchaseDto getPurchaseDetails(int purchaseId){
        try{
            String query = "SELECT * FROM ProductPurchase where id=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1,purchaseId);

                ResultSet rs = stmt.executeQuery(query);
                PurchaseDto purchase = null;
                if (rs.next()) {
                    purchase = new PurchaseDto();
                    purchase.setId(rs.getInt("Id"));
                    purchase.setPurchaseDate(rs.getTimestamp("PurchaseDate").toLocalDateTime());
                    purchase.setAmount(rs.getDouble("Amount"));
                    purchase.setPurchasedBy(rs.getInt("PurchasedBy"));
                    purchase.setDealerName(rs.getString("DealerEmail"));
                    purchase.setDealerContact(rs.getString("DealerMobileNumber"));
                    purchase.setPurchaseList(getPurchaseHistoryDetails(purchaseId));
                    return purchase;
                }
            }
        }catch (Exception e){
            System.out.println("Error in Purchase Details");
        }
        return null;
    }

    public List<PurchaseHistoryDto> getPurchaseHistoryDetails(int purchaseId) throws SQLException{
        String query = "SELECT * FROM productpurchasehistory where purchaseid=?";
        List<PurchaseHistoryDto> purchases = new ArrayList<>();
        try{
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1,purchaseId);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    PurchaseHistoryDto purchase = new PurchaseHistoryDto();
                    purchase.setId(rs.getInt("Id"));
                    purchase.setPurchaseId(rs.getInt("purchaseid"));
                    purchase.setExpired(rs.getBoolean("isexpired"));
                    purchase.setExpiryDate(rs.getTimestamp("expirydate").toLocalDateTime());
                    purchase.setAmount(rs.getDouble("Amount"));
                    purchase.setProductId(rs.getInt("productid"));
                    purchases.add(purchase);
                }
            }
        }catch (Exception e){
            System.out.println("Error in Purchase List");
        }
        return purchases;
    }


}
