package Services;

public class NetBankingService extends PaymentService {
    public NetBankingService() {
        super("Net Banking");
    }

    @Override
    public String processPayment(double amount) {
        logPaymentDetails(amount);
        System.out.println("Processing net banking payment of " + amount);
        return generateReferenceNumber();
    }

    public String processPayment(double amount,String accountNumber){
        logPaymentDetails(amount);
//        System.out.println("Processing net banking payment of " + amount +"to "+accountNumber);
        return generateReferenceNumber();
    }

}
