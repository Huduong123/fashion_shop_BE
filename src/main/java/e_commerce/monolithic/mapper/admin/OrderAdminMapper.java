package e_commerce.monolithic.mapper.admin;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.admin.order.OrderAdminResponseDTO;
import e_commerce.monolithic.entity.Order;
import e_commerce.monolithic.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderAdminMapper {
    private final PaymentMethodAdminMapper paymentMethodAdminMapper;
    private final OrderMapper orderMapper;
    public OrderAdminResponseDTO convertToOrderAdminDTO(Order order){
        if (order == null) {
            return null;
        }

        return OrderAdminResponseDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(paymentMethodAdminMapper.convertToPaymentMethodAdminDTO(order.getPaymentMethod()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalItems(order.getOrderItems().size())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .userEmail(order.getUser().getEmail())
                .userFullname(order.getUser().getFullname())
                .userPhone(order.getUser().getPhone())
                .orderItems(order.getOrderItems().stream()
                        .map(orderMapper::convertToOrderItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
