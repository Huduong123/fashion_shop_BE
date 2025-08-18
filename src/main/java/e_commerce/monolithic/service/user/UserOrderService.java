package e_commerce.monolithic.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;

public interface UserOrderService {

    // lấy danh sách đơn hàng tóm tắt cho người dùng (phân trang)
    Page<OrderSummaryDTO> getOrderSummariesForUser(String username, Pageable pageable);
    
    // lấy danh sách đơn hàng chi tiết (phân trang)
    Page<OrderResponseDTO> getOrderDetailsPageForUser(String username, Pageable pageable);

    // tạo đơn đơn hàng từ giỏ hàng
    OrderResponseDTO createOrderFromCart(String username);

    // lấy chi tiết 1 đơn hàng cụ thể.
    OrderResponseDTO getOrderDetails(String username, Long orderId);

    // hủy đơn hàng
    ResponseMessageDTO cancelOrder(String username, Long orderid);

}
