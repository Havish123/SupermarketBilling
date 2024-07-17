package Services;

import DTO.OrderDetailDto;
import DTO.ProductCartDto;
import Extension.DatabaseUtil;
import Interfaces.ICartService;
import Models.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartService implements ICartService {
    static Connection conn = DatabaseUtil.getConnection();

    public int addCartItem(Cart cart) throws SQLException {
        String checkQuery = "SELECT COUNT(1) FROM Carts WHERE userid = ? and productid=? and ispurchased=false";
        PreparedStatement checkPs = conn.prepareStatement(checkQuery);
        checkPs.setInt(1, cart.getCustomer().getId());
        checkPs.setInt(2, cart.getProduct().getId());
        ResultSet checkRs = checkPs.executeQuery();

        if(checkRs.next() && checkRs.getInt(1) > 0){
            System.out.println("Product Already Exists in the cart");
            return 0;

        }
        String query = "INSERT INTO Carts (productId, ispurchased, userid, quantity, createddate, createdby, modifieddate) VALUES (?, ?, ?, ?, NOW(), ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getProduct().getId());
            stmt.setBoolean(2, cart.isPurchased());
            stmt.setInt(3, cart.getCustomer().getId());
            stmt.setDouble(4, cart.getQuantity());
            stmt.setInt(5, cart.getCustomer().getId());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cart.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return rowsInserted;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<ProductCartDto> getProductIdsByCartIds(Set<Integer> cartIds) {
        List<ProductCartDto> productIds = new ArrayList<>();
        if (cartIds == null || cartIds.isEmpty()) {
            return productIds;
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT productid,id as cartid FROM Carts WHERE Id IN (");
        for (int i = 0; i < cartIds.size(); i++) {
            queryBuilder.append("?");
            if (i < cartIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i=0;
            for (int cartId : cartIds) {
                stmt.setInt(i + 1, cartId);
                i++;
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductCartDto cartDto=new ProductCartDto();
                    cartDto.setCartId(rs.getInt("cartid"));
                    cartDto.setProductId(rs.getInt("productid"));
                    productIds.add(cartDto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productIds;
    }

    public void updateCartItem(List<OrderDetailDto> orders) {
        for(OrderDetailDto order:orders){
            String query = "UPDATE Carts SET isPurchased = ?, quantity = ?, modifieddate = NOW() WHERE Id = ? ";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBoolean(1, true);
                stmt.setDouble(2, order.getQuantity());
                stmt.setInt(3, order.getCartId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int deleteCartItem(Set<Integer> cartIds) {
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM Carts WHERE Id IN (");
        for (int i = 0; i < cartIds.size(); i++) {
            queryBuilder.append("?");
            if (i < cartIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i=0;
            for(int cartId :cartIds){
                stmt.setInt(i + 1, cartId);
                i++;
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Cart> getCartItemsByUserId(int userId) {
        List<Cart> cartItems = new ArrayList<>();
        String query = "SELECT c.id,c.productid,c.ispurchased,c.userid,c.quantity,p.name as productname,(p.price * c.quantity) as price FROM Carts c join products p on p.id=c.productId WHERE userId = ? and ispurchased=false";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cart cart=new Cart();
                    cart.setId(rs.getInt("id"));
                    cart.getCustomer().setId(rs.getInt("userid"));
                    cart.getProduct().setId(rs.getInt("productid"));
                    cart.getProduct().setName(rs.getString("productname"));
                    cart.setPurchased(rs.getBoolean("ispurchased"));
                    cart.setQuantity(rs.getDouble("quantity"));
                    cart.setPrice(rs.getDouble("price"));
                    cartItems.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

}

