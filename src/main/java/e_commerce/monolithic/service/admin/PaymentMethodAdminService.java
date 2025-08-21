package e_commerce.monolithic.service.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.PaymentMethod;

public interface PaymentMethodAdminService {
    List<PaymentMethodAdminDTO> getAllPaymentMethods();

    PaymentMethodAdminDTO getPaymentMethodById(Long id);

    PaymentMethodAdminDTO createPaymentMethod(PaymentMethodCreateAdminDTO createDTO, MultipartFile file) throws IOException;

    PaymentMethodAdminDTO updatePaymentMethod(Long id, PaymentMethodUpdateAdminDTO updateAdminDTO, MultipartFile file) throws IOException;

    ResponseMessageDTO deletePaymentMethod(Long id);
}
