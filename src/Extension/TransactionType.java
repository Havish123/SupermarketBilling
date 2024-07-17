package Extension;

import java.util.HashMap;
import java.util.Map;


public enum TransactionType {
    Debit(1),
    Credit(2);
    private static final Map<Integer, TransactionType> map = new HashMap<>();

    static {
        for (TransactionType type : TransactionType.values()) {
            map.put(type.value, type);
        }
    }
    private final int value;

    TransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static String getNameByValue(int value) {
        TransactionType type = map.get(value);
        return type != null ? type.name().toLowerCase() : null;
    }
}
