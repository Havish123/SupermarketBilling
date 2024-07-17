package Services;

public class UPIService extends PaymentService {
    public UPIService() {
        super("UPI");
    }

    @Override
    public String processPayment(double amount) {
        logPaymentDetails(amount);
        System.out.println("Processing UPI payment of " + amount);
        return generateReferenceNumber();
    }
}
