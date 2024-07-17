package Services;

import Extension.DatabaseUtil;
import Extension.DiscountType;
import Models.Coupon;
import Models.Discount;
import Models.Product;
import Models.ProductDiscount;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiscountService {

    static Connection conn = DatabaseUtil.getConnection();

    // Discount for Purchase
    public int addOrUpdateDiscount(Discount discount){
        try {
            if (discount.getId() == 0) {
                String query = "INSERT INTO Discount (DiscountType, Percentage, Description, ValidFromDate, ValidToDate, CreatedDate, CreatedBy,minpurchaseamount) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,?)";
                try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, discount.getDiscountType());
                    stmt.setDouble(2, discount.getPercentage());
                    stmt.setString(3, discount.getDescription());
                    stmt.setTimestamp(4, Timestamp.valueOf(discount.getValidFromDate()));
                    stmt.setTimestamp(5, Timestamp.valueOf(discount.getValidToDate()));
                    stmt.setInt(6, discount.getCreatedBy());
                    stmt.setInt(7, discount.getMinPurchaseAmount());

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                return generatedKeys.getInt(1);
                            }
                        }
                    }
                }
            } else {
                String query = "UPDATE Discount SET  Percentage = ?, Description = ?, ValidFromDate = ?, ValidToDate = ?, ModifiedDate = CURRENT_TIMESTAMP, ModifiedBy = ?,minpurchaseamount=? WHERE Id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                    stmt.setString(1, discount.getDiscountType());
                    stmt.setDouble(1, discount.getPercentage());
                    stmt.setString(2, discount.getDescription());
                    stmt.setTimestamp(3, Timestamp.valueOf(discount.getValidFromDate()));
                    stmt.setTimestamp(4, Timestamp.valueOf(discount.getValidToDate()));
                    stmt.setInt(5, discount.getModifiedBy());
                    stmt.setInt(6, discount.getId());
                    stmt.setInt(7, discount.getMinPurchaseAmount());

                    return stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Discount> discountList(){
        List<Discount> discounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM Discount";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Discount discount = new Discount();
                    discount.setId(rs.getInt("Id"));
                    discount.setDiscountType(DiscountType.getNameByValue(Integer.parseInt(rs.getString("DiscountType"))));
                    discount.setPercentage(rs.getDouble("Percentage"));
                    discount.setDescription(rs.getString("Description"));
                    discount.setValidFromDate(rs.getTimestamp("ValidFromDate").toLocalDateTime());
                    discount.setValidToDate(rs.getTimestamp("ValidToDate").toLocalDateTime());
                    discount.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                    discount.setCreatedBy(rs.getInt("CreatedBy"));
                    discount.setModifiedDate(rs.getTimestamp("ModifiedDate").toLocalDateTime());
                    discount.setModifiedBy(rs.getInt("ModifiedBy"));
                    discount.setMinPurchaseAmount(rs.getInt("minpurchaseamount"));
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    public Discount getDiscountForPrice(double totalAmount){
        Discount discount=new Discount();
        double discountAmount = 0.0;
        try {
            String query = "SELECT * FROM Discount WHERE ValidFromDate <= CURRENT_TIMESTAMP AND ValidToDate >= CURRENT_TIMESTAMP AND minpurchaseamount <= ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1,totalAmount);
                ResultSet rs = stmt.executeQuery();


                if (rs.next()) {
                    discount.setId(rs.getInt("Id"));
                    discount.setMinPurchaseAmount(rs.getInt("minpurchaseamount"));
                    discount.setDescription(rs.getString("description"));
                    discount.setPercentage(rs.getDouble("Percentage"));
//                    double percentage = rs.getDouble("Percentage");
//                    discountAmount = totalAmount * (percentage / 100);
                }else{
                    discount=null;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }


    public Coupon getCouponData(String couponCode){
        Coupon discount=new Coupon();
        double discountAmount = 0.0;
        try {
            String query = "select Id,isused,validfromdate,validtodate,code,value,availableclaims from coupon c where c.code=? AND validtodate::date >= Now()::date and validfromdate <= now()::date and isused=false and availableclaims>0";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1,couponCode);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    discount.setId(rs.getInt("Id"));
                    discount.setUsed(rs.getBoolean("isused"));
                    discount.setValidfromdate(rs.getTimestamp("validfromdate").toLocalDateTime());
                    discount.setValidtodate(rs.getTimestamp("validtodate").toLocalDateTime());
                    discount.setCode(rs.getString("code"));
                   discount.setValue(rs.getDouble("value"));
                   discount.setAvailableclaims(rs.getInt("availableclaims"));
                }else{
                    discount=null;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }
    public boolean hasUserUsedCoupon(int customerId, int couponId) {
        String query = "SELECT COUNT(1) FROM orders WHERE customerid = ? AND couponid = ?";
        boolean hasUsedCoupon = false;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, couponId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    hasUsedCoupon = (count > 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasUsedCoupon;
    }

    public Discount getDiscountDetails(int discountId){
        Discount discount = null;
        try {
            String query = "SELECT * FROM Discount WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, discountId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        discount = new Discount();
                        discount.setId(rs.getInt("Id"));
                        discount.setDiscountType(rs.getString("DiscountType"));
                        discount.setPercentage(rs.getDouble("Percentage"));
                        discount.setDescription(rs.getString("Description"));
                        discount.setValidFromDate(rs.getTimestamp("ValidFromDate").toLocalDateTime());
                        discount.setValidToDate(rs.getTimestamp("ValidToDate").toLocalDateTime());
                        discount.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                        discount.setCreatedBy(rs.getInt("CreatedBy"));
                        discount.setModifiedDate(rs.getTimestamp("ModifiedDate").toLocalDateTime());
                        discount.setModifiedBy(rs.getInt("ModifiedBy"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }

    public int deleteDiscount(int discountId) {
        try {
            String query = "DELETE FROM Discount WHERE Id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, discountId);
                return stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean checkDiscountExists(LocalDateTime startDate, LocalDateTime endDate, boolean isupdate,int discountId) {
        String query =isupdate ?"SELECT COUNT(1) FROM Discount WHERE ValidFromDate <= ? AND ValidToDate >= ? AND Id<> ?": "SELECT COUNT(1) FROM Discount WHERE ValidFromDate <= ? AND ValidToDate >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(endDate));
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            if(isupdate){
                stmt.setInt(3,discountId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Product Discount

    public int deleteProductDiscount(int discountId) {
        String query = "DELETE FROM ProductDiscount WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, discountId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkProductDiscountExists(LocalDateTime startDate, LocalDateTime endDate,int productId,boolean isupdate,int discountId) {
        String query =isupdate ?"SELECT COUNT(1) FROM ProductDiscount WHERE ValidFromDate <= ? AND ValidToDate >= ? AND ProductId=? AND Id<> ?" : "SELECT COUNT(1) FROM ProductDiscount WHERE ValidFromDate <= ? AND ValidToDate >= ? AND ProductId=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(endDate));
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setInt(3, productId);
            if(isupdate){
                stmt.setInt(4, discountId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ProductDiscount getProductDiscountDetails(int discountId) {
        ProductDiscount discount = null;
        String query = "SELECT * FROM ProductDiscount WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, discountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    discount = new ProductDiscount();
                    discount.setId(rs.getInt("Id"));
                    discount.setPercentage(rs.getDouble("Percentage"));
                    discount.getProduct().setId(rs.getInt("ProductId"));
                    discount.setDescription(rs.getString("Description"));
                    discount.setValidFromDate(rs.getTimestamp("ValidFromDate").toLocalDateTime());
                    discount.setValidToDate(rs.getTimestamp("ValidToDate").toLocalDateTime());
                    discount.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                    discount.setModifiedDate(rs.getTimestamp("ModifiedDate").toLocalDateTime());
                    discount.setCreatedBy(rs.getInt("CreatedBy"));
                    discount.setModifiedBy(rs.getInt("ModifiedBy"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }

    public ProductDiscount getDiscountsForProducts(int productId) {
        ProductDiscount productDiscount = null;
        String query = "SELECT * FROM ProductDiscount WHERE ProductId = ? AND ValidFromDate <= CURRENT_TIMESTAMP AND ValidToDate >= CURRENT_TIMESTAMP";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productDiscount = new ProductDiscount();
                    productDiscount.setId(rs.getInt("Id"));
                    productDiscount.setPercentage(rs.getDouble("Percentage"));
                    productDiscount.getProduct().setId(rs.getInt("ProductId"));
                    productDiscount.setDescription(rs.getString("Description"));
                    productDiscount.setValidFromDate(rs.getTimestamp("ValidFromDate").toLocalDateTime());
                    productDiscount.setValidToDate(rs.getTimestamp("ValidToDate").toLocalDateTime());
                    productDiscount.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                    productDiscount.setModifiedDate(rs.getTimestamp("ModifiedDate").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDiscount;
    }

    public List<ProductDiscount> getProductDiscountList() {
        List<ProductDiscount> discounts = new ArrayList<>();
        String query = "SELECT * FROM ProductDiscount";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ProductDiscount discount = new ProductDiscount();
                discount.setId(rs.getInt("Id"));
                discount.setPercentage(rs.getDouble("Percentage"));
                discount.getProduct().setId(rs.getInt("ProductId"));
                discount.setDescription(rs.getString("Description"));
                discount.setValidFromDate(rs.getTimestamp("ValidFromDate").toLocalDateTime());
                discount.setValidToDate(rs.getTimestamp("ValidToDate").toLocalDateTime());
                discount.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                discount.setModifiedDate(rs.getTimestamp("ModifiedDate").toLocalDateTime());
                discount.setCreatedBy(rs.getInt("CreatedBy"));
                discount.setModifiedBy(rs.getInt("ModifiedBy"));
                discounts.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    public int addOrUpdateProductDiscount(ProductDiscount productDiscount) {
        String query = productDiscount.getId() == 0 ?
                "INSERT INTO ProductDiscount (Percentage, ProductId, Description, ValidFromDate, ValidToDate, CreatedBy,CreatedDate) VALUES (?, ?, ?, ?, ?, ?,NOW())" :
                "UPDATE ProductDiscount SET Percentage = ?, ProductId = ?, Description = ?, ValidFromDate = ?, ValidToDate = ?, ModifiedBy = ?, ModifiedDate = CURRENT_TIMESTAMP WHERE Id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, productDiscount.getPercentage());
            stmt.setInt(2, productDiscount.getProduct().getId());
            stmt.setString(3, productDiscount.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(productDiscount.getValidFromDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(productDiscount.getValidToDate()));
            stmt.setInt(6, productDiscount.getCreatedBy());

            if (productDiscount.getId() != 0) {
                stmt.setInt(8, productDiscount.getId());
            }

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0 && productDiscount.getId() == 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return productDiscount.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public int addCoupon(Coupon coupon, int currentUserId) {
        String query = "INSERT INTO Coupon (value, description, isUsed, validFromDate, validToDate, createdBy, code, totalClaims, availableClaims) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, coupon.getValue());
            stmt.setString(2, coupon.getDescription());
            stmt.setBoolean(3, coupon.isUsed());
            stmt.setTimestamp(4, Timestamp.valueOf(coupon.getValidfromdate()));
            stmt.setTimestamp(5, Timestamp.valueOf(coupon.getValidtodate()));
            stmt.setInt(6, currentUserId);
            stmt.setString(7, coupon.getCode());
            stmt.setInt(8, coupon.getTotalclaims());
            stmt.setInt(9, coupon.getAvailableclaims());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int updateCouponClaims(int couponId, int totalClaims, int availableClaims) {
        String query = "UPDATE Coupon SET totalClaims = ?, availableClaims = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, totalClaims);
            stmt.setInt(2, availableClaims);
            stmt.setInt(3, couponId);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Coupon> listCoupons(int currentUserId) {
        List<Coupon> coupons = new ArrayList<>();
        String query = "SELECT * FROM Coupon";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getInt("id"));
                coupon.setValue(rs.getDouble("value"));
                coupon.setDescription(rs.getString("description"));
                coupon.setUsed(rs.getBoolean("isused"));
                coupon.setValidfromdate(rs.getTimestamp("validfromdate").toLocalDateTime());
                coupon.setValidtodate(rs.getTimestamp("validtodate").toLocalDateTime());
                coupon.setCode(rs.getString("code"));
                coupon.setTotalclaims(rs.getInt("totalclaims"));
                coupon.setAvailableclaims(rs.getInt("availableclaims"));
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coupons;
    }


}
