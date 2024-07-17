package Services;

public class DebitCardService extends PaymentService {
    public DebitCardService() {
        super("Debit Card");
    }

    @Override
    public String processPayment(double amount) {
        logPaymentDetails(amount);
        System.out.println("Processing debit card payment of " + amount);
        return generateReferenceNumber();
    }
}
