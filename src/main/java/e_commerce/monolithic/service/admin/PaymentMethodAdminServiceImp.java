package e_commerce.monolithic.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodCreateAdminDTO;
import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodUpdateAdminDTO;
import e_commerce.monolithic.entity.PaymentMethod;
import e_commerce.monolithic.exeption.DuplicateResourceException;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.admin.PaymentMethodAdminMapper;
import e_commerce.monolithic.repository.PaymentMethodRepository;

@Service
public class PaymentMethodAdminServiceImp implements PaymentMethodAdminService{

    private final PaymentMethodAdminMapper paymentMethodAdminMapper;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodAdminServiceImp(PaymentMethodAdminMapper paymentMethodAdminMapper,
            PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodAdminMapper = paymentMethodAdminMapper;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethodAdminDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(paymentMethodAdminMapper::convertToPaymentMethodAdminDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public PaymentMethodAdminDTO createPaymentMethod(PaymentMethodCreateAdminDTO createDTO) {

        // BƯỚC 1: Chuẩn hóa 'code' thành dạng slug (chữ thường, gạch nối)
        String normalizedCode = createDTO.getCode().toLowerCase().replaceAll("\\s+", "-");

        // BƯỚC 2: Sử dụng code đã được chuẩn hóa để kiểm tra trùng lặp
        paymentMethodRepository.findByCode(normalizedCode).ifPresent(pm -> {
            // Ném lỗi với code gốc để người dùng dễ nhận biết
            throw new DuplicateResourceException(
                "Code '" + createDTO.getCode() + "' đã tồn tại (dưới dạng '" + normalizedCode + "'). Vui lòng chọn một code khác."
            );
        });

        // BƯỚC 3: Kiểm tra trùng lặp 'name' 
        paymentMethodRepository.findByName(createDTO.getName()).ifPresent(pm -> {
            throw new DuplicateResourceException("Tên phương thức thanh toán '" + createDTO.getName() + "' đã tồn tại.");
        });

        // BƯỚC 4: Chuyển đổi DTO sang Entity
        PaymentMethod newPaymentMethod = paymentMethodAdminMapper.convertCreateDTOToEntity(createDTO);
        
        // BƯỚC 5: Gán code đã được chuẩn hóa cho Entity trước khi lưu
        newPaymentMethod.setCode(normalizedCode);

        // BƯỚC 6: Lưu và trả về kết quả
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(newPaymentMethod);
        return paymentMethodAdminMapper.convertToPaymentMethodAdminDTO(savedPaymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodAdminDTO updatePaymentMethod(Long id, PaymentMethodUpdateAdminDTO updateDTO) {
        // BƯỚC 1: Tìm phương thức thanh toán cần cập nhật. Nếu không thấy, ném lỗi 404.
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phương thức thanh toán với ID: " + id));

        // BƯỚC 2: Kiểm tra xem 'name' mới có bị trùng với một payment method khác không.
        // Chỉ kiểm tra nếu 'name' thực sự bị thay đổi.
        if (!existingPaymentMethod.getName().equals(updateDTO.getName())) {
            paymentMethodRepository.findByName(updateDTO.getName()).ifPresent(pm -> {
                throw new DuplicateResourceException("Tên phương thức thanh toán '" + updateDTO.getName() + "' đã tồn tại.");
            });
        }
        
        // BƯỚC 3: Dùng mapper để cập nhật các trường của đối tượng đã tồn tại.
        paymentMethodAdminMapper.convertUpdateDTOToEntity(updateDTO, existingPaymentMethod);

        // BƯỚC 4: Lưu lại vào DB. JPA sẽ tự động thực hiện câu lệnh UPDATE.
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);

        // BƯỚC 5: Chuyển đổi Entity đã cập nhật thành DTO để trả về cho client.
        return paymentMethodAdminMapper.convertToPaymentMethodAdminDTO(savedPaymentMethod);
    }

    
}
