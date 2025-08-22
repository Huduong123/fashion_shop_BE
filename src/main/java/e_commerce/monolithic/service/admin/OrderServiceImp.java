package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.order.OrderAdminResponseDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderItemResponseDTO;
import e_commerce.monolithic.entity.Order;
import e_commerce.monolithic.entity.OrderItem;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.admin.OrderAdminMapper;
import e_commerce.monolithic.repository.OrderRepository;
import e_commerce.monolithic.repository.ProductVariantRepository;
import e_commerce.monolithic.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
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
@Slf4j
public class OrderServiceImp implements OrderService {

        private final OrderRepository orderRepository;
        private final ProductVariantRepository productVariantRepository;
        private final OrderAdminMapper orderAdminMapper;
        @Override
        public Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable, String username, OrderStatus status,
                        LocalDate startDate, LocalDate endDate) {
                Specification<Order> spec = OrderSpecification.getAllOrdersSpec(username, status, startDate, endDate);
                Page<Order> orderPage = orderRepository.findAll(spec, pageable);

                return orderPage.map(orderAdminMapper::convertToOrderAdminDTO);
        }

        @Override
        public OrderAdminResponseDTO getOrderById(Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

                return orderAdminMapper.convertToOrderAdminDTO(order);
        }

        @Override
        @Transactional
        public ResponseMessageDTO updateOrderStatus(Long orderId, OrderStatus status) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

                order.setStatus(status);
                orderRepository.save(order);

                return new ResponseMessageDTO(HttpStatus.OK, "Order status updated successfully");
        }

        @Override
        @Transactional
        public ResponseMessageDTO deleteOrder(Long orderId) {
            // TRUY VẤN 1: Lấy Order và các OrderItem liên quan
            Order order = orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    
            if (order.getOrderItems().isEmpty()) {
                // Nếu không có sản phẩm nào, chỉ cần xóa đơn hàng
                orderRepository.delete(order);
                return new ResponseMessageDTO(HttpStatus.OK, "Order deleted successfully (no items to restore).");
            }
    
            // Lấy ra danh sách các ProductVariant từ các OrderItem
            List<ProductVariant> variantsInOrder = order.getOrderItems().stream()
                    .map(OrderItem::getProductVariant)
                    .distinct()
                    .collect(Collectors.toList());
    
            // TRUY VẤN 2: Tải sẵn (fetch) các ProductVariantSize cho các variant ở trên
            // Mặc dù chúng ta không sử dụng kết quả trả về của câu lệnh này,
            // nó sẽ "làm giàu" các đối tượng ProductVariant đã có trong Persistence Context.
            productVariantRepository.findWithSizesByVariants(variantsInOrder);
    
            // Bây giờ, các đối tượng productVariant bên trong `order` đã có đủ dữ liệu `productVariantSizes`
            // mà không gây ra lỗi lazy loading.
    
            // Hoàn lại số lượng sản phẩm vào kho
            for (OrderItem item : order.getOrderItems()) {
                var productVariant = item.getProductVariant();
                var sizeId = item.getSize().getId();
                int quantityToRestore = item.getQuantity();
    
                productVariant.getProductVariantSizes().stream()
                    .filter(pvs -> pvs.getSize().getId().equals(sizeId))
                    .findFirst()
                    .ifPresentOrElse(
                        productVariantSize -> {
                            log.info("Restoring stock for Variant ID: {}, Size ID: {}. Current stock: {}. Restoring: {}",
                                    productVariant.getId(), sizeId, productVariantSize.getQuantity(), quantityToRestore);
                            productVariantSize.setQuantity(productVariantSize.getQuantity() + quantityToRestore);
                        },
                        () -> log.warn("Could not find ProductVariantSize for Variant ID: {} and Size ID: {} to restore stock.",
                                       productVariant.getId(), sizeId)
                    );
            }
    
            // Xóa đơn hàng
            orderRepository.delete(order);
    
            return new ResponseMessageDTO(HttpStatus.OK, "Order deleted successfully and stock restored.");
        }

  
}