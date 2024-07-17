package Controller;

import DTO.PurchaseDto;
import DTO.UserDto;
import Extension.Roles;
import Models.Category;
import Models.Product;
import Services.InventoryService;
import Services.SalesService;
import views.Inventory.ProductPage;
import views.Sales.BillingPage;

import javax.crypto.AEADBadTagException;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {

    InventoryService _service=new InventoryService();

    public int addProduct(Product product){
        try{
            return _service.addProduct(product);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int updateProduct(Product product,int currentuserId){
        try{
            return _service.updateProduct(product,currentuserId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int deleteproduct(int ProductId){
        try{
            return _service.deleteProduct(ProductId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int deletecategory(int categoryId){
        try{
            return _service.deleteCategory(categoryId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int addCategory(Category category){
        try{
            return _service.addCategory(category);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<Category> getCategoryList(){
        List<Category> categoryList=new ArrayList<>();
        try{
            categoryList=_service.getCategoryList();
        }catch (Exception e){
            e.printStackTrace();
        }
       return categoryList;
    }

    public int customerBilling(UserDto currentUser,int categoryId){
        List<Product> productList=new ArrayList<>();
        try{
            productList=_service.getProductList(categoryId);
            ProductPage.displayProductListForBilling(productList);
            int productId=ProductPage.chooseProductPage();

            if(productId!=0){
                Product product=this.getProductById(productId);
                int option=BillingPage.displayProductDetails(product);
                if(option!=0){
                    if(option==1){

                    }else if(option==2){

                    }else {
                        System.out.println("Invalid Input, Try Again.");
                    }
                }
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public Product getProductById(int productId){
        Product product;
        try{
            product=_service.getProductById(productId);
            return product;
        }catch (Exception e){
            System.out.println("Problem with getting Product details");
        }
        return null;
    }

    public List<Product> showProductList(UserDto currentUser,int categoryId){
        List<Product> productList=new ArrayList<>();
        try{
            productList=_service.getProductList(categoryId);
            if(currentUser.getRoleId()== Roles.Customer.getValue()){
                ProductPage.displayProductListForBilling(productList);
            }else{
                ProductPage.displayProductList(productList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return productList;
    }

    public void addpurchase(int currentUserId){
        try{
            PurchaseDto input=new PurchaseDto();
            input=ProductPage.addPurchaseItem(currentUserId);

            int result=_service.addPurchaseData(input);
            if(result>0){
                System.out.println("Purchase Added Successfully");
            }else{
                System.out.println("Error in add purchase");
            }
        }catch (Exception e){
            System.out.println("Error in Purchase Add.");
        }
    }

    public void showPurchaseList(int currentUserid){
        try{
            List<PurchaseDto> purchaselist=new ArrayList<>();
            purchaselist=_service.getPurchaseList();
            ProductPage.displayPurchaseList(purchaselist);
        }catch (Exception e){
            System.out.println("Error in purchaseList");
        }
    }

    public void showExpireProductList(int currentUserid){
        try{
            List<PurchaseDto> purchaselist=new ArrayList<>();
            purchaselist=_service.getPurchaseList();
            ProductPage.displayPurchaseList(purchaselist);
        }catch (Exception e){
            System.out.println("Error in purchaseList");
        }
    }
}
