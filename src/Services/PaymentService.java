package Services;

import Interfaces.IPaymentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class PaymentService implements IPaymentService {
    protected String paymentType;

    public PaymentService(String paymentType) {
        this.paymentType = paymentType;
    }

    protected void logPaymentDetails(double amount) {
//        System.out.println("Processing " + paymentType + " payment of " + amount);
    }

    public static String generateReferenceNumber() {
        String prefix = "REF";
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);

        return prefix + uniqueId + timestamp;
    }

    @Override
    public abstract String processPayment(double amount);


}
