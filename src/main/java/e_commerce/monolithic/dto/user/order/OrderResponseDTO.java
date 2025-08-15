package e_commerce.monolithic.dto.user.order;

import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.entity.enums.PaymentStatus;
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

public class OrderResponseDTO {

  private Long id;
  private BigDecimal totalPrice;
  private OrderStatus status;

  private PaymentMethodDTO paymentMethod;
  private PaymentStatus paymentStatus;
  private LocalDateTime createdAt;


  private List<OrderItemResponseDTO> orderItems;

}
