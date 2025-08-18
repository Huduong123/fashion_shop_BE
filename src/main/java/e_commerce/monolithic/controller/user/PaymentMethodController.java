package e_commerce.monolithic.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.user.payment_method.PaymentMethodDTO;
import e_commerce.monolithic.service.user.UserPaymentMethodService;

@RestController
@RequestMapping("/api/users/payment-methods")
public class PaymentMethodController {
    private final UserPaymentMethodService paymentMethodService;

    public PaymentMethodController(UserPaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getActivePaymentMethods() {
        List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getActivePaymentMethods();
        return ResponseEntity.ok(paymentMethodDTOs);
    }

}
