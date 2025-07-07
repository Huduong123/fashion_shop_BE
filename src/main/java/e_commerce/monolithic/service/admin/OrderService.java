package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.order.OrderAdminResponseDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderService {

    Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable, String username, OrderStatus status,
            LocalDate startDate, LocalDate endDate);

    OrderAdminResponseDTO getOrderById(Long orderId);

    ResponseMessageDTO updateOrderStatus(Long orderId, OrderStatus status);

    ResponseMessageDTO deleteOrder(Long orderId);
}