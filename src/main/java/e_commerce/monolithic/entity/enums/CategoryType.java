package e_commerce.monolithic.entity.enums;

/**
 * Enum for Category Types
 * DROPDOWN - Category chỉ để phân nhóm, không thể chứa sản phẩm trực tiếp
 * LINK - Category có thể chứa sản phẩm trực tiếp
 */
public enum CategoryType {
    DROPDOWN("dropdown", "Thư mục"),
    LINK("link", "Liên kết");

    private final String value;
    private final String displayName;

    CategoryType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoryType fromValue(String value) {
        for (CategoryType type : CategoryType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown category type: " + value);
    }
} 