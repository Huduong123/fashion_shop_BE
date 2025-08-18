package e_commerce.monolithic.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.user.payment_method.PaymentMethodDTO;


public interface UserPaymentMethodService {

    List<PaymentMethodDTO> getActivePaymentMethods();
    
}
