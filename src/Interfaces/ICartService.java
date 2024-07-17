package Interfaces;

import DTO.OrderDetailDto;
import DTO.ProductCartDto;
import Models.Cart;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface ICartService {
    public int addCartItem(Cart cart) throws SQLException;
    public List<Cart> getCartItemsByUserId(int userId);
    public int deleteCartItem(Set<Integer> cartIds);
    public void updateCartItem(List<OrderDetailDto> orders);
    public List<ProductCartDto> getProductIdsByCartIds(Set<Integer> cartIds);
}
