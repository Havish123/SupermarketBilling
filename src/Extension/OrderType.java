package Extension;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
    Online(1),
    Store(2);

    private final int value;

    private static final Map<Integer, OrderType> map = new HashMap<>();

    static {
        for (OrderType ordertype : OrderType.values()) {
            map.put(ordertype.value, ordertype);
        }
    }
    OrderType(int value) {
        this.value = value;
    }
    public static String getNameByValue(int value) {
        OrderType orderType = map.get(value);
        return orderType != null ? orderType.name().toLowerCase() : null;
    }
    public int getValue() {
        return value;
    }
}
