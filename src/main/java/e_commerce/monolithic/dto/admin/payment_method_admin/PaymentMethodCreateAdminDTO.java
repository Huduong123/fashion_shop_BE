package e_commerce.monolithic.dto.admin.payment_method_admin;

import e_commerce.monolithic.entity.enums.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodCreateAdminDTO {
    @NotBlank(message = "Code không được để trống")
    private String code;

    @NotBlank(message = "Tên phương thức thanh toán không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    private String imageUrl;
    @NotNull(message = "Loại phương thức thanh toán không được để trống")
    private PaymentMethodType type;

    private boolean enabled;
}
