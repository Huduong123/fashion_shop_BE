package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.order.OrderAdminResponseDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.entity.Order;
import e_commerce.monolithic.entity.OrderItem;
import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.repository.OrderRepository;
import e_commerce.monolithic.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable, String username, OrderStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<Order> spec = OrderSpecification.getAllOrdersSpec(username, status, startDate, endDate);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        
        return orderPage.map(this::convertToOrderAdminResponseDTO);
    }

    @Override
    public OrderAdminResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        
        return convertToOrderAdminResponseDTO(order);
    }

    @Override
    @Transactional
    public ResponseMessageDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        
        order.setStatus(status);
        orderRepository.save(order);
        
        return ResponseMessageDTO.builder()
                .message("Order status updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        
        orderRepository.delete(order);
        
        return ResponseMessageDTO.builder()
                .message("Order deleted successfully")
                .build();
    }

    private OrderAdminResponseDTO convertToOrderAdminResponseDTO(Order order) {
        return OrderAdminResponseDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalItems(order.getOrderItems().size())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .userEmail(order.getUser().getEmail())
                .userFullname(order.getUser().getFullname())
                .userPhone(order.getUser().getPhone())
                .orderItems(order.getOrderItems().stream()
                        .map(this::convertToOrderItemResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private OrderItemResponseDTO convertToOrderItemResponseDTO(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .productVariantId(orderItem.getProductVariant().getId())
                .productName(orderItem.getProductVariant().getProduct().getName())
                .colorName(orderItem.getProductVariant().getColor().getName())
                .sizeName(orderItem.getProductVariant().getSize() != null ? 
                        orderItem.getProductVariant().getSize().getName() : "N/A")
                .imageUrl(orderItem.getProductVariant().getImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
} 