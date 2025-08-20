package e_commerce.monolithic.service.admin;

import java.util.List;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

public interface PaymentMethodAdminService {
    List<PaymentMethodAdminDTO> getAllPaymentMethods();
    PaymentMethodAdminDTO createPaymentMethod(PaymentMethodCreateAdminDTO createDTO);

    PaymentMethodAdminDTO updatePaymentMethod(Long id, PaymentMethodUpdateAdminDTO updateAdminDTO);

    ResponseMessageDTO deletePaymentMethod(Long id);
}
