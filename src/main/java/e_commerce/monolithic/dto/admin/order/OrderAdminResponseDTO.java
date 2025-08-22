package e_commerce.monolithic.dto.admin.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import e_commerce.monolithic.dto.admin.payment_method_admin.PaymentMethodAdminDTO;
import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAdminResponseDTO {

    private Long id;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalItems;
    
    private PaymentStatus paymentStatus;
    private PaymentMethodAdminDTO paymentMethod;

    // User information
    private Long userId;
    private String username;
    private String userEmail;
    private String userFullname;
    private String userPhone;
    
    // Order items
    private List<OrderItemResponseDTO> orderItems;
} 