package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import e_commerce.monolithic.entity.Order;
import e_commerce.monolithic.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderItemResponseDTO convertToOrderItemDTO(OrderItem orderItem) {

        var variant = orderItem.getProductVariant();
        var product = variant.getProduct();

        return OrderItemResponseDTO.builder()
                .productVariantId(variant.getId())
                .productName(product.getName())
                .colorName(variant.getColor().getName())
                .sizeName(variant.getSize().getName())
                .imageUrl(variant.getImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    public OrderResponseDTO convertToOrderDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .orderItems(order.getOrderItems().stream()
                        .map(this::convertToOrderItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderSummaryDTO convertToOrderSummaryDTO(Order order) {
        return OrderSummaryDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .totalItems(order.getOrderItems().size())
                .build();
    }
}
