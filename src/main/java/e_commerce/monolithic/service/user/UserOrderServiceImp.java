package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.order.OrderResponseDTO;
import e_commerce.monolithic.dto.user.order.OrderSummaryDTO;
import e_commerce.monolithic.entity.*;
import e_commerce.monolithic.entity.enums.OrderStatus;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.OrderMapper;
import e_commerce.monolithic.repository.CartItemRepository;
import e_commerce.monolithic.repository.OrderRepository;
import e_commerce.monolithic.repository.ProductVariantRepository;
import e_commerce.monolithic.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;

@Service
public class UserOrderServiceImp implements UserOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderMapper orderMapper;

    public UserOrderServiceImp(OrderRepository orderRepository, UserRepository userRepository, CartItemRepository cartItemRepository, ProductVariantRepository productVariantRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDTO> getOrdersForUser(String username, Pageable pageable) {
        User user =findUserByUsername(username);
        Page<Order> ordersPage = orderRepository.findByUserId(user.getId(), pageable);

        return ordersPage.map(orderMapper::convertToOrderSummaryDTO);
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrderFromCart(String username) {
        User user = findUserByUsername(username);
        List<CartItem> cartItems = findAndValidateCartItem(user.getId());

        Order newOrder = buildOrderFromCart(user, cartItems);

        // lưu đơn hàng và xóa ở giỏ hàng
        orderRepository.save(newOrder);
        cartItemRepository.deleteByUserId(user.getId());
        Order finalOrder = findOrderByIdAndUserIdWithDetails(newOrder.getId(), user.getId());
        return orderMapper.convertToOrderDTO(finalOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderDetails(String username, Long orderId) {
        User user = findUserByUsername(username);

        Order order = findOrderByIdAndUserIdWithDetails(orderId, user.getId());

        return orderMapper.convertToOrderDTO(order);
    }

    @Override
    @Transactional
    public ResponseMessageDTO cancelOrder(String username, Long orderid) {
        User user = findUserByUsername(username);
        Order order = findOrderById(orderid);

        // kiểm tra quyền và điều kiện hủy
        checkOwnerShip(order, user);
        checkIfCancellable(order);
        order.setStatus(OrderStatus.CANCELLED);
        restoreStockForCancelledOrder(order);

        orderRepository.save(order);

        return new ResponseMessageDTO(HttpStatus.OK, "Đã hủy đơn hàng thành công");
    }

    // hàm check
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng: " + username));
    }

    // tìm order theo id
    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));
    }

    // tìm order chi tiết theo id và user id
    private Order findOrderByIdAndUserIdWithDetails(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserIdWithDetails(orderId, userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiết đơn hàng với ID: " + orderId));
    }

    // tìm các sản phẩm trong giỏ hàng và check giỏ hàng có rỗng không
    private List<CartItem> findAndValidateCartItem(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserIdWithDetails(userId);
        if (cartItems.isEmpty()) {
            throw  new IllegalArgumentException("Giỏ hàng của bạn đang trống. Không th tạo đơn hàng");
        }
        return cartItems;
    }

    /**
     * Xây dựng đối tượng Order từ giỏ hàng , bao gồm việc kiểm tra và trừ tồn kho
     */
    private Order buildOrderFromCart(User user, List<CartItem> cartItems) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(OrderStatus.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductVariant productVariant = cartItem.getProductVariant();
            int requestedQuantity = cartItem.getQuantity();

            checkAndReduceStock(productVariant, requestedQuantity);

            OrderItem orderItem = createOrderItem(newOrder, productVariant, requestedQuantity);

            newOrder.getOrderItems().add(orderItem);

            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(requestedQuantity)));
        }
        newOrder.setTotalPrice(totalPrice);
        return newOrder;


    }

    // kiểm tra tồn kho và giản số lyownjg sản phẩm
    private void checkAndReduceStock(ProductVariant productVariant, int requestedQuantity) {
        if (productVariant.getQuantity() < requestedQuantity) {
            throw new IllegalArgumentException(String.format("Sảm phẩm '%s' không đủ số lượng. Chỉ còn %d sản phẩm trong kho.",
                    productVariant.getProduct().getName(), productVariant.getQuantity()));
        }
        productVariant.setQuantity(productVariant.getQuantity() - requestedQuantity);
    }

    // tạo 1 đối tượng order item
    private OrderItem createOrderItem(Order order, ProductVariant productVariant, int quantity) {
        return OrderItem.builder()
                .order(order)
                .productVariant(productVariant)
                .quantity(quantity)
                .price(productVariant.getPrice())
                .build();
    }
    // kiêm tra user có phải là chủ sỡ hữu order không
    private void checkOwnerShip(Order order, User user) {
        if(!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyeefn thực hiện thao tác trên đơn hàng này");
        }
    }

    // kiểm tra xem đơn hàng có ở trạng thái cho phép hủy hay không
    private void checkIfCancellable(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Không thể hủy đơn hàng ở trạng thái '" + order.getStatus() + "'.");
        }
    }

    // hoàn trả lại số lượng sản phẩm vào kho khi đơn hàng bị hủy
    private void restoreStockForCancelledOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            ProductVariant productVariant = item.getProductVariant();
            productVariant.setQuantity(productVariant.getQuantity() + item.getQuantity());
        }
    }


}
