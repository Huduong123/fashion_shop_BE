package e_commerce.monolithic.controller.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import e_commerce.monolithic.service.user.UserOrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final UserOrderService userOrderService;

    /**
     * API 1 (KHUYÊN DÙNG): Lấy danh sách đơn hàng TÓM TẮT.
     * Tối ưu cho trang "Lịch sử mua hàng".
     * GET /api/users/orders/summaries?page=0&size=10&sortBy=createdAt&sortDir=desc
     */
    @GetMapping("/summaries")
    public ResponseEntity<Page<OrderSummaryDTO>> getUserOrderSummaries(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        String username = authentication.getName();
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderSummaryDTO> orders = userOrderService.getOrderSummariesForUser(username, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * API 2 (GIỮ LẠI): Lấy danh sách đơn hàng CHI TIẾT.
     * Chỉ sử dụng khi có nhu cầu đặc biệt cần tải nhiều chi tiết cùng lúc.
     * GET /api/users/orders?page=0&size=10&sortBy=createdAt&sortDir=desc
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getUserOrderDetailsPage(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        String username = authentication.getName();
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Gọi đến phương thức service tương ứng trả về chi tiết
        Page<OrderResponseDTO> orders = userOrderService.getOrderDetailsPageForUser(username, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * API 3: Lấy chi tiết MỘT đơn hàng cụ thể.
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
     * API 4: Tạo một đơn hàng mới từ giỏ hàng.
     * POST /api/users/orders
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(Authentication authentication) {
        String username = authentication.getName();
        OrderResponseDTO createdOrder = userOrderService.createOrderFromCart(username);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * API 5: Người dùng tự hủy đơn hàng.
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