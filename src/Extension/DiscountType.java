package Extension;

import java.util.HashMap;
import java.util.Map;

public enum DiscountType {
    UPI(1),
    CreditCard(2),
    DebitCard(3),
    Amount(4);
    private static final Map<Integer, DiscountType> map = new HashMap<>();

    static {
        for (DiscountType type : DiscountType.values()) {
            map.put(type.value, type);
        }
    }
    private final int value;

    DiscountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static String getNameByValue(int value) {
        DiscountType type = map.get(value);
        return type != null ? type.name().toLowerCase() : null;
    }
}
