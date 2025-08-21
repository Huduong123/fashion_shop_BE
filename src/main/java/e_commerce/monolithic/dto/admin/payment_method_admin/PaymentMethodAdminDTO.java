package e_commerce.monolithic.dto.admin.payment_method_admin;

import java.time.LocalDateTime;

import e_commerce.monolithic.entity.enums.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodAdminDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private PaymentMethodType type;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
