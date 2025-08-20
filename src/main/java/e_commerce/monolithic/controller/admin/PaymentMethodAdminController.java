package e_commerce.monolithic.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.service.admin.PaymentMethodAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/payment-methods")
public class PaymentMethodAdminController {

    private final PaymentMethodAdminService paymentMethodAdminService;


    public PaymentMethodAdminController(PaymentMethodAdminService paymentMethodAdminService) {
        this.paymentMethodAdminService = paymentMethodAdminService;
    }
    @GetMapping
    public ResponseEntity<List<PaymentMethodAdminDTO>> getAllPaymentMethod() {
        List<PaymentMethodAdminDTO> paymentMethodAdminDTOs = paymentMethodAdminService.getAllPaymentMethods();
        return ResponseEntity.ok(paymentMethodAdminDTOs);
    }
    @PostMapping
    public ResponseEntity<PaymentMethodAdminDTO> createPaymentMethod(@RequestBody PaymentMethodCreateAdminDTO createAdminDTO) {
        PaymentMethodAdminDTO newPaymentMethodAdminDTO = paymentMethodAdminService.createPaymentMethod(createAdminDTO);
        return ResponseEntity.ok(newPaymentMethodAdminDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodAdminDTO> updatepaymentMethod(@PathVariable Long id,
                                                                    @Valid @RequestBody PaymentMethodUpdateAdminDTO updateAdminDTO) {
        PaymentMethodAdminDTO updatePaymentMethod = paymentMethodAdminService.updatePaymentMethod(id, updateAdminDTO);
        return ResponseEntity.ok(updatePaymentMethod);

    }
}
