package Controller;

import Interfaces.IPaymentService;
import Services.CreditCardService;
import Services.DebitCardService;
import Services.NetBankingService;
import Services.UPIService;

public class PaymentController {
    IPaymentService paymentService;


    public String ProcessPayment(int paymentType,double amount){

        String referenceKey="";
        try{
            switch (paymentType){
                case 1:
                    return "REF" + System.currentTimeMillis();
                case 2:
                    paymentService=new UPIService();
                    referenceKey=paymentService.processPayment(amount);
                    break;
                case 3:
                    paymentService=new DebitCardService();
                    referenceKey=paymentService.processPayment(amount);
                    break;
                case 4:
                    paymentService=new CreditCardService();
                    referenceKey=paymentService.processPayment(amount);
                    break;
                case 5:
                    paymentService=new NetBankingService();
                    referenceKey=paymentService.processPayment(amount);
                    break;
                default:
                    System.out.println("Invalid Input.Try Again");
            }
        }catch (Exception e){
            System.out.println("Error in Payment Service.Try Again");
        }

        return referenceKey;
    }
}
