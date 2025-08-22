package e_commerce.monolithic.dto.user.payment_method;

import e_commerce.monolithic.entity.enums.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private PaymentMethodType type;
}
