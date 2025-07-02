package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import e_commerce.monolithic.service.user.UserOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/orders")
public class UserOrderController {

    private final UserOrderService userOrderService;

    public UserOrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    /**
     * API 1: Lấy danh sách đơn hàng của người dùng (phân trang).
     * GET /api/users/orders?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<Page<OrderSummaryDTO>> getUserOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        String username = authentication.getName();
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<OrderSummaryDTO> orders = userOrderService.getOrdersForUser(username, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * API 2: Tạo một đơn hàng mới từ giỏ hàng.
     * POST /api/users/orders
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(Authentication authentication) {
        String username = authentication.getName();
        OrderResponseDTO createdOrder = userOrderService.createOrderFromCart(username);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * API 3: Lấy chi tiết một đơn hàng cụ thể.
     * GET /api/users/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        OrderResponseDTO orderDetails = userOrderService.getOrderDetails(username, id);
        return ResponseEntity.ok(orderDetails);
    }

    /**
     * API 4: Người dùng tự hủy đơn hàng.
     * POST /api/users/orders/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ResponseMessageDTO> cancelOrder(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        ResponseMessageDTO response = userOrderService.cancelOrder(username, id);
        return ResponseEntity.ok(response);
    }
}