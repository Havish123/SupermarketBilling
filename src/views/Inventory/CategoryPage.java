package views.Inventory;

import Models.Category;
import Services.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoryPage {
    public static void displayCategoryListForCustomer(List<Category> categorylist){

        System.out.println(String.format("%-15s  %-20s %-15s","Category No","Name","Code"));
        for(Category cate :categorylist){
            System.out.println(cate.toString());
        }
        System.out.println("Enter the Category No, you want to select");
        System.out.println("Enter 0: Exit");
    }
    public static void displayCategoryList(List<Category> categorylist){
        System.out.println(String.format("%-15s  %-20s %-15s","CategoryId","Name","Code"));
        for(Category cate :categorylist){
            System.out.println(cate.toString());
        }
    }
    public static int deleteCategory(){
        int categoryId=0;
        Scanner scanner=new Scanner(System.in);

        System.out.println("Enter Category Id: ");
        categoryId=scanner.nextInt();

        return categoryId;
    }
    public static Category addCategory(){
        Scanner userInput = new Scanner(System.in);
        Category category=new Category();
        System.out.println("Enter Category Name");
        category.setName(userInput.nextLine());
        InventoryService service=new InventoryService();
        List<Category> categoryList=new ArrayList<>();
        if(categoryList.size()==0){
            System.out.println("Enter Category Code");
            category.setCode(userInput.nextLine());
        }else{
            while (true){
                System.out.println("Enter Category Code");
                category.setCode(userInput.nextLine());
                Category ctg = categoryList.stream()
                        .filter(i -> i.getCode() == category.getCode())
                        .findFirst()
                        .orElse(null);
                if (ctg != null) {
                    System.out.println("Code Already Exists");
                }else{
                    break;
                }
            };
        }

        return category;
    }
}
