package Services;

public class CreditCardService extends PaymentService{
    public CreditCardService() {
        super("Credit Card");
    }

    @Override
    public String processPayment(double amount) {
        logPaymentDetails(amount);
        System.out.println("Processing credit card payment of " + amount);
        return generateReferenceNumber();
    }

}
