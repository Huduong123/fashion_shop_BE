package e_commerce.monolithic.mapper;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.user.order.PaymentMethodDTO;
import e_commerce.monolithic.entity.PaymentMethod;

@Component
public class PaymentMethodMapper {

    public PaymentMethodDTO convertToPaymentMethodDTO (PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return PaymentMethodDTO.builder()
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .build();
    }
}
