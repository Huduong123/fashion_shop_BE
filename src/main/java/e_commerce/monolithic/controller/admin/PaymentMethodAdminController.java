package e_commerce.monolithic.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
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

    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodAdminDTO> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodAdminDTO paymentMethodAdminDTO = paymentMethodAdminService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethodAdminDTO);
    } 

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<PaymentMethodAdminDTO> createPaymentMethod(
            @RequestPart("data") @Valid PaymentMethodCreateAdminDTO createAdminDTO,
            @RequestPart("file") MultipartFile file
            ) throws IOException {
        PaymentMethodAdminDTO newPaymentMethodAdminDTO = paymentMethodAdminService.createPaymentMethod(createAdminDTO, file);
        return ResponseEntity.ok(newPaymentMethodAdminDTO);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<PaymentMethodAdminDTO> updatepaymentMethod(@PathVariable Long id,
            @RequestPart("data") @Valid  PaymentMethodUpdateAdminDTO updateAdminDTO,
            @RequestPart(value =  "file", required = false) MultipartFile file
            ) throws IOException {
        PaymentMethodAdminDTO updatePaymentMethod = paymentMethodAdminService.updatePaymentMethod(id, updateAdminDTO, file);
        return ResponseEntity.ok(updatePaymentMethod);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> deletePaymentMethod(@PathVariable Long id) {
        ResponseMessageDTO responseMessageDTO = paymentMethodAdminService.deletePaymentMethod(id);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
