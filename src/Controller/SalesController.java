package Controller;

import DTO.MyOrderDto;
import DTO.OrderDetailDto;
import DTO.OrderDto;
import DTO.UserDto;
import Extension.OrderType;
import Models.Customer;
import Models.Order;
import Models.Product;
import Services.SalesService;
import views.Sales.BillingPage;
import views.User.CustomerPage;
import views.User.SignupPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SalesController {
    SalesService _service=new SalesService();

    public int billProduct(int currenctUserId){
        try{

            OrderDto order=BillingPage.billProduct(currenctUserId);
            return _service.saveOrders(order);

        }catch (Exception e){
            System.out.println("Error in BillPage-"+e.getMessage());
            return 0;
        }

    }

    public int checkoutProduct(Set<Integer> productIds, UserDto user){
        try{

            OrderDto order=BillingPage.checkoutProduct(user,productIds);
            if(order!=null){
                return _service.saveOrders(order);
            }else {
                return 0;
            }


        }catch (Exception e){
            System.out.println("Error in BillPage-"+e.getMessage());
            return 0;
        }
    }

    public int checkoutProductByCartIds(Set<Integer> cartids, UserDto user){
        try{

            OrderDto order=BillingPage.checkoutProductByCartIds(user,cartids);
            if(order==null){
                return 0;
            }else{
                int result=_service.saveOrders(order);
                if(result>0 && order.orderType== OrderType.Online.getValue()){
                    CartController controller=new CartController();
                    controller.updateCartItem(order.orderdetails);
                }
                return result;
            }


        }catch (Exception e){
            System.out.println("Error in BillPage-"+e.getMessage());
            return 0;
        }
    }

    public void showMyOrders(int userId){
        try{
            int limit=10,offset=0;
            while (true){
                List<MyOrderDto> myorders=_service.getOrdersByuserId(userId,limit,offset);

                if(myorders.size()>0){
                    int totalpages=(int)Math.ceil((double) myorders.get(0).getTotalCount()/limit);

                    CustomerPage.displayMyOrders(myorders,totalpages,offset);

                    int choice=CustomerPage.displayOrdersMenu(totalpages,offset);

                    if(choice==0){
                        break;
                    }

                    switch (choice){
                        case 1:
                            if(totalpages!=offset+1){
                                offset++;
                            }else{
                                System.out.println("Invalid Input");
                            }
                            break;
                        case 2:
                            if((offset+1)!=1){
                                offset--;

                            }else{
                                System.out.println("Invalid Input");
                            }
                            break;
                        case 3:
                            int billnumber=BillingPage.billdetails();
                            if(billnumber!=0){
                                OrderDto order=_service.getOrderbyBillNumber(billnumber,userId);
                                if(order==null){
                                    System.out.println("Invalid Billnumber");
                                }else{
                                    BillingPage.showBillDetails(order);
                                }

                            }
                            break;
                        default:
                            System.out.println("Invalid Input");
                            break;
                    }
                }else{
                    System.out.println("No Record Found");
                    break;
                }


            };




        }catch (Exception e){
            System.out.println("Sales - Error while Processing your request");
        }
    }

    public Product getProductPrice(int productId){
        Product product=null;
        try{
            Customer customer = new Customer();
            SignupPage.SignupUser(customer);
            return _service.getProductPrice(productId);
        }
        catch (Exception e){
            System.out.println("Error in Get Product Price"+ e.getMessage());
        }

        return  product;
    }
}
