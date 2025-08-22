package e_commerce.monolithic.mapper;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.user.payment_method.PaymentMethodDTO;
import e_commerce.monolithic.entity.PaymentMethod;

@Component
public class PaymentMethodMapper {

    public PaymentMethodDTO convertToPaymentMethodDTO(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return PaymentMethodDTO.builder()
                .id(paymentMethod.getId())
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .imageUrl(paymentMethod.getImageUrl())
                .type(paymentMethod.getType())
                .build();
    }
}
