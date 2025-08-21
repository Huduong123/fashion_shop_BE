package e_commerce.monolithic.mapper.admin;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.entity.PaymentMethod;

@Component
public class PaymentMethodAdminMapper {
    
    public PaymentMethodAdminDTO convertToPaymentMethodAdminDTO (PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        return PaymentMethodAdminDTO.builder()
                .id(paymentMethod.getId()) 
                .code(paymentMethod.getCode())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .imageUrl(paymentMethod.getImageUrl())
                .type(paymentMethod.getType())
                .enabled(paymentMethod.isEnabled())
                .createdAt(paymentMethod.getCreatedAt())
                .updatedAt(paymentMethod.getUpdatedAt())
                .build();
    }

    public PaymentMethod convertCreateDTOToEntity(PaymentMethodCreateAdminDTO paymentMethodCreateAdminDTO) {
        if (paymentMethodCreateAdminDTO == null) {
            return null;
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCode(paymentMethodCreateAdminDTO.getCode());
        paymentMethod.setName(paymentMethodCreateAdminDTO.getName());
        paymentMethod.setDescription(paymentMethodCreateAdminDTO.getDescription());
        paymentMethod.setImageUrl(paymentMethodCreateAdminDTO.getImageUrl());
        paymentMethod.setType(paymentMethodCreateAdminDTO.getType());
        paymentMethod.setEnabled(paymentMethodCreateAdminDTO.isEnabled());
        return paymentMethod;
    }

    public void convertUpdateDTOToEntity(PaymentMethodUpdateAdminDTO paymentMethodUpdateAdminDTO, PaymentMethod paymentMethod) {
    
        if (paymentMethodUpdateAdminDTO == null || paymentMethod == null) {
            return;
        }
        paymentMethod.setName(paymentMethodUpdateAdminDTO.getName());
        paymentMethod.setDescription(paymentMethodUpdateAdminDTO.getDescription());
        paymentMethod.setImageUrl(paymentMethodUpdateAdminDTO.getImageUrl());
        paymentMethod.setType(paymentMethodUpdateAdminDTO.getType());
        paymentMethod.setEnabled(paymentMethodUpdateAdminDTO.getEnabled());
    }

}
