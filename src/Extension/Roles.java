package Extension;

import java.util.HashMap;
import java.util.Map;

public enum Roles {
    Admin(1),
    Cashier(2),
    Customer(3);
    private static final Map<Integer, Roles> map = new HashMap<>();

    static {
        for (Roles role : Roles.values()) {
            map.put(role.value, role);
        }
    }
    private final int value;

    Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static String getNameByValue(int value) {
        Roles role = map.get(value);
        return role != null ? role.name().toLowerCase() : null;
    }
}
