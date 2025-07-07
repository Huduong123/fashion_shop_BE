package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.order.OrderAdminResponseDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.service.admin.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Get all orders with filtering and pagination
     * GET
     * /api/admin/orders?page=0&size=10&sort=createdAt,desc&username=john&status=PENDING
     */
    @GetMapping
    public ResponseEntity<Page<OrderAdminResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Parse sort parameter (format: "field,direction")
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<OrderAdminResponseDTO> orders = orderService.getAllOrders(pageable, username, status, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID
     * GET /api/admin/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAdminResponseDTO> getOrderById(@PathVariable Long id) {
        OrderAdminResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Update order status
     * PUT /api/admin/orders/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseMessageDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        ResponseMessageDTO response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete order
     * DELETE /api/admin/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> deleteOrder(@PathVariable Long id) {
        ResponseMessageDTO response = orderService.deleteOrder(id);
        return ResponseEntity.ok(response);
    }
}