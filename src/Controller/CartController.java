package Controller;

import DTO.OrderDetailDto;
import DTO.ProductCartDto;
import Interfaces.ICartService;
import Models.Cart;
import Services.CartService;
import Services.ReportService;
import views.Sales.BillingPage;
import views.User.CustomerPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartController {
    ICartService _service = new CartService();

    public int addCartItem(Cart cart) {
        try {
            return _service.addCartItem(cart);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<ProductCartDto> getProductIdsbyCartId(Set<Integer> cartIds){
        List<ProductCartDto> productIds=new ArrayList<>();

        productIds=_service.getProductIdsByCartIds(cartIds);

        return  productIds;
    }

    public int showCartItemsByUserId(int userId) {
        try {
            List<Cart> cartlist=_service.getCartItemsByUserId(userId);
            return  CustomerPage.showCartList(cartlist);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void updateCartItem(List<OrderDetailDto> orders) {
        try {
            _service.updateCartItem(orders);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int deleteCartItem(Set<Integer> cartIds) {
        try {
            return _service.deleteCartItem(cartIds);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
