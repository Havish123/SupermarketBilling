package Controller;

import Models.Coupon;
import Models.Discount;
import Models.Product;
import Models.ProductDiscount;
import Services.DiscountService;
import views.Inventory.DiscountPage;
import views.Inventory.ProductDiscountPage;

import java.util.List;

public class DiscountController {

    DiscountService _service = new DiscountService();


    public void addDiscount(int currentUserId){
        try{
            Discount discount=new Discount();
            discount=DiscountPage.addDiscount(discount);
            if(discount==null){
                return;
            }
            discount.setCreatedBy(currentUserId);

            if(_service.checkDiscountExists(discount.getValidFromDate(),discount.getValidToDate(),false,0)){
                System.out.println("Already Discount Assigned for this date.");
                return;
            }

            int result=_service.addOrUpdateDiscount(discount);
            if(result>0){
                System.out.println("Discount Added Successfully");
            }else{
                System.out.println("Discount not created. Please try after some time");
            }
        }catch (Exception e){
            System.out.println("Error in Discount Creation");
        }

    }
    public void updateDiscount(int currentUserId){
        try{
            Discount discount=new Discount();
            discount=DiscountPage.updateDiscount(_service);
            discount.setModifiedBy(currentUserId);
            if(_service.checkDiscountExists(discount.getValidFromDate(),discount.getValidToDate(),true,discount.getId())){
                System.out.println("Discount Already Exists for the date range");
                return;
            }
            int result=_service.addOrUpdateDiscount(discount);
            if(result>0){
                System.out.println("Discount Updated Successfully");
            }else{
                System.out.println("Discount not created. Please try after some time");
            }
        }catch (Exception e){
            System.out.println("Error in Discount Creation");
        }

    }
    public void viewDiscountList(){
        try{
            List<Discount> discountlist=_service.discountList();
            DiscountPage.displayDiscounts(discountlist);
        }catch (Exception e){
            System.out.println("Error in view Discounts");
        }
    }
    public void addProductDiscount(int currentUserId){
        try{
            ProductDiscount discount=new ProductDiscount();
            discount= ProductDiscountPage.addProductDiscount(discount);
            discount.setCreatedBy(currentUserId);
            if(_service.checkProductDiscountExists(discount.getValidFromDate(),discount.getValidToDate(),discount.getProduct().getId(),false,0)){
                System.out.println("Already Discount Assigned for this date.");
                return;
            }
            int result=_service.addOrUpdateProductDiscount(discount);
            if(result>0){
                System.out.println("Discount Added Successfully");
            }else{
                System.out.println("Discount not created. Please try after some time");
            }
        }catch (Exception e){
            System.out.println("Error in Discount Creation");
        }

    }
    public void updateProductDiscount(int currentUserId){
        try{
            ProductDiscount discount=new ProductDiscount();
            discount=ProductDiscountPage.updateProductDiscount(_service);

            if(_service.checkProductDiscountExists(discount.getValidFromDate(),discount.getValidToDate(),discount.getProduct().getId(),true,discount.getId())){
                System.out.println("Discount Already Exist for the date range");
                return;
            }
            discount.setModifiedBy(currentUserId);
            int result=_service.addOrUpdateProductDiscount(discount);
            if(result>0){
                System.out.println("Discount Updated Successfully");
            }else{
                System.out.println("Discount not created. Please try after some time");
            }
        }catch (Exception e){
            System.out.println("Error in Discount Creation");
        }

    }
    public void viewProductDiscountList(){
        try{
            List<ProductDiscount> discountlist=_service.getProductDiscountList();
            ProductDiscountPage.displayProductDiscounts(discountlist);

        }catch (Exception e){
            System.out.println("Error in view Discounts");
        }
    }

    public void addCoupon(int currentUserId){
        try{
            Coupon coupon=new Coupon();
            coupon=DiscountPage.addCoupon(coupon);
            if(coupon==null){
                return;
            }
            int result=_service.addCoupon(coupon,currentUserId);
            if(result>0){
                System.out.println("Coupon Added Successfully");
            }else{
                System.out.println("Coupon not created. Please try after some time");
            }
        }catch (Exception e){
            System.out.println("Error in Coupon Creation");
        }

    }

    public void showCouponList(int currentUserId){
        try{
            List<Coupon> discountlist=_service.listCoupons(currentUserId);
            DiscountPage.displayCoupons(discountlist);

        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in view Discounts");
        }
    }
}
