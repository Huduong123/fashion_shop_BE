package e_commerce.monolithic.entity.enums;

/**
 * Enum for Category Status
 * ACTIVE - Category đang hoạt động, hiển thị cho user
 * INACTIVE - Category bị vô hiệu hóa, không hiển thị cho user
 */
public enum CategoryStatus {
    ACTIVE("active", "Hoạt động"),
    INACTIVE("inactive", "Vô hiệu hóa");

    private final String value;
    private final String displayName;

    CategoryStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoryStatus fromValue(String value) {
        for (CategoryStatus status : CategoryStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown category status: " + value);
    }
}