package e_commerce.monolithic.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import e_commerce.monolithic.entity.Order;
import e_commerce.monolithic.entity.OrderItem;

@Component
public class OrderMapper {

    private final PaymentMethodMapper paymentMethodMapper;

    public OrderMapper(PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodMapper = paymentMethodMapper;
    }

    public OrderItemResponseDTO convertToOrderItemDTO(OrderItem orderItem) {

        var variant = orderItem.getProductVariant();
        var product = variant.getProduct();
        var size = orderItem.getSize();

        return OrderItemResponseDTO.builder()
                .productVariantId(variant.getId())
                .productName(product.getName())
                .colorName(variant.getColor().getName())
                .sizeName(size.getName())
                .imageUrl(variant.getPrimaryImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    public OrderResponseDTO convertToOrderDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(paymentMethodMapper.convertToPaymentMethodDTO(order.getPaymentMethod()))
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .orderItems(order.getOrderItems().stream()
                        .map(this::convertToOrderItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderSummaryDTO convertToOrderSummaryDTO(Order order, int totalItems) {
        return OrderSummaryDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(paymentMethodMapper.convertToPaymentMethodDTO(order.getPaymentMethod()))
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .totalItems(totalItems)
                .build();
    }
}
