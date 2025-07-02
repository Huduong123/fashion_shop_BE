package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserOrderService {

    // lấy danh sách đơn hàng (phân trang)
    Page<OrderSummaryDTO> getOrdersForUser(String username, Pageable pageable);

    // tạo đơn đơn hàng từ giỏ hàng
    OrderResponseDTO createOrderFromCart(String username);

    // lấy chi tiết 1 đơn hàng.
    OrderResponseDTO getOrderDetails(String username, Long orderId);

    // hủy đơn hàng
    ResponseMessageDTO cancelOrder(String username, Long orderid);


}
