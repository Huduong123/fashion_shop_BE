package e_commerce.monolithic.dto.admin.order;

import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    
    // User information
    private Long userId;
    private String username;
    private String userEmail;
    private String userFullname;
    private String userPhone;
    
    // Order items
    private List<OrderItemResponseDTO> orderItems;
} 