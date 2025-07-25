package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemCreateDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemResponseDTO;
import e_commerce.monolithic.entity.CartItem;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantSize;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.CartItemMapper;
import e_commerce.monolithic.repository.CartItemRepository;
import e_commerce.monolithic.repository.ProductVariantRepository;
import e_commerce.monolithic.repository.SizeRepository;
import e_commerce.monolithic.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SizeRepository sizeRepository;
    private final CartItemMapper cartItemMapper;

    public CartServiceImp(CartItemRepository cartItemRepository, UserRepository userRepository,
            ProductVariantRepository productVariantRepository, SizeRepository sizeRepository,
            CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.sizeRepository = sizeRepository;
        this.cartItemMapper = cartItemMapper;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với username: " + username));
    }

    private CartItem getCartItemForUser(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Không tìm thấy sản phẩm với ID " + cartItemId + " trong giỏ hàng của bạn."));
    }

    /**
     * Lấy entity ProductVariant từ ID, nếu không tìm thấy sẽ ném ra exception.
     * 
     * @param variantId ID của biến thể sản phẩm.
     * @return Entity ProductVariant.
     */
    private ProductVariant getProductVariantById(Long variantId) {
        return productVariantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm với ID: " + variantId));
    }

    /**
     * Lấy entity Size từ ID, nếu không tìm thấy sẽ ném ra exception.
     * 
     * @param sizeId ID của size.
     * @return Entity Size.
     */
    private Size getSizeById(Long sizeId) {
        return sizeRepository.findById(sizeId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy size với ID: " + sizeId));
    }

    /**
     * Kiểm tra số lượng tồn kho của sản phẩm theo size cụ thể.
     * 
     * @param variant           Biến thể sản phẩm cần kiểm tra.
     * @param sizeId            ID của size cần kiểm tra.
     * @param requestedQuantity Số lượng yêu cầu.
     */
    private void checkStockAvailability(ProductVariant variant, Long sizeId, int requestedQuantity) {
        Optional<ProductVariantSize> productVariantSize = variant.getProductVariantSizes().stream()
                .filter(pvs -> pvs.getSize().getId().equals(sizeId))
                .findFirst();

        if (productVariantSize.isEmpty()) {
            throw new IllegalArgumentException("Size không có sẵn cho sản phẩm này.");
        }

        int availableQuantity = productVariantSize.get().getQuantity();
        if (availableQuantity < requestedQuantity) {
            throw new IllegalArgumentException(
                    "Không đủ số lượng sản phẩm trong kho. Chỉ còn " + availableQuantity + " sản phẩm cho size này.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDTO> getCartItems(String username) {
        User user = getUserByUsername(username);
        List<CartItem> cartItems = cartItemRepository.findByUserIdWithDetails(user.getId());
        return cartItems.stream()
                .map(cartItemMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDTO> getRecentCartItems(String username, int limit) {
        User user = getUserByUsername(username);
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));
        List<CartItem> cartItems = cartItemRepository.findRecentByUserIdWithDetails(user.getId(), pageable);

        return cartItems.stream()
                .map(cartItemMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItemResponseDTO addProductToCart(String username, CartItemCreateDTO createDTO) {
        // 1. Lấy thông tin cần thiết
        User user = getUserByUsername(username);
        ProductVariant variant = getProductVariantById(createDTO.getProductVariantId());
        Size size = getSizeById(createDTO.getSizeId());

        // 2. Kiểm tra xem sản phẩm với size cụ thể đã có trong giỏ hàng chưa
        Optional<CartItem> existingCartItemOpt = cartItemRepository
                .findByUserIdAndProductVariantIdAndSizeId(user.getId(), variant.getId(), size.getId());

        CartItem cartItemToSave;
        if (existingCartItemOpt.isPresent()) {
            // 3a. Nếu đã có, cập nhật số lượng
            cartItemToSave = existingCartItemOpt.get();
            int newQuantity = cartItemToSave.getQuantity() + createDTO.getQuantity();

            checkStockAvailability(variant, size.getId(), newQuantity); // Kiểm tra tồn kho
            cartItemToSave.setQuantity(newQuantity);
        } else {
            // 3b. Nếu chưa có, tạo mới
            checkStockAvailability(variant, size.getId(), createDTO.getQuantity()); // Kiểm tra tồn kho
            cartItemToSave = CartItem.builder()
                    .user(user)
                    .productVariant(variant)
                    .size(size)
                    .quantity(createDTO.getQuantity())
                    .build();
        }

        // 4. Lưu và trả về kết quả
        CartItem savedCartItem = cartItemRepository.save(cartItemToSave);
        return cartItemMapper.convertToDTO(savedCartItem);
    }

    @Override
    @Transactional
    public CartItemResponseDTO increaseItemQuantity(String username, Long cartItemId) {
        // 1. Lấy thông tin
        User user = getUserByUsername(username);
        CartItem cartItem = getCartItemForUser(cartItemId, user.getId());
        ProductVariant variant = cartItem.getProductVariant();
        Size size = cartItem.getSize();

        // 2. Kiểm tra tồn kho trước khi tăng
        checkStockAvailability(variant, size.getId(), cartItem.getQuantity() + 1);

        // 3. Tăng số lượng và lưu
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        CartItem updatedItem = cartItemRepository.save(cartItem);

        // 4. Trả về DTO (đã sửa kiểu trả về)
        return cartItemMapper.convertToDTO(updatedItem);
    }

    @Override
    @Transactional
    public CartItemResponseDTO decreaseItemQuantity(String username, Long cartItemId) {
        // 1. Lấy thông tin
        User user = getUserByUsername(username);
        CartItem cartItem = getCartItemForUser(cartItemId, user.getId());

        int currentQuantity = cartItem.getQuantity();

        // 2. Nếu số lượng là 1, giảm sẽ thành xóa
        if (currentQuantity <= 1) {
            cartItemRepository.delete(cartItem);
            // Trả về DTO của item đã xóa với số lượng là 0 để frontend xử lý
            CartItemResponseDTO dto = cartItemMapper.convertToDTO(cartItem);
            dto.setQuantity(0);
            return dto;
        }

        // 3. Nếu lớn hơn 1, giảm số lượng và lưu
        cartItem.setQuantity(currentQuantity - 1);
        CartItem updatedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.convertToDTO(updatedItem);
    }

    @Override
    @Transactional
    public ResponseMessageDTO removeCartItem(String username, Long cartItemId) {
        User user = getUserByUsername(username);
        CartItem cartItem = getCartItemForUser(cartItemId, user.getId());
        cartItemRepository.delete(cartItem);
        return new ResponseMessageDTO(HttpStatus.OK, "Xóa thành công sản phẩm với Id: " + cartItemId);
    }

    @Override
    public ResponseMessageDTO clearCart(String username) {

        User user = getUserByUsername(username);
        cartItemRepository.deleteByUserId(user.getId());

        return new ResponseMessageDTO(HttpStatus.OK, "Đã xóa tất cả sản phẩm khỏi giỏ hàng thành công");
    }

     @Override
    @Transactional // Đảm bảo tất cả các hoạt động được thực hiện trong một giao dịch duy nhất
    public ResponseMessageDTO syncCart(String username, List<CartItemCreateDTO> itemsToSync) {
        if (itemsToSync == null || itemsToSync.isEmpty()) {
            return new ResponseMessageDTO(HttpStatus.OK, "Không có sản phẩm nào để đồng bộ.");
        }

        // Lặp qua từng sản phẩm từ giỏ hàng của khách
        for (CartItemCreateDTO item : itemsToSync) {
            try {
                // Tái sử dụng hoàn toàn logic thêm sản phẩm đã có
                // Nó sẽ tự động kiểm tra và cộng dồn số lượng nếu cần
                addProductToCart(username, item);
            } catch (Exception e) {
                // Ghi log lỗi nhưng vẫn tiếp tục với các sản phẩm khác
                // Hoặc bạn có thể quyết định dừng lại và báo lỗi ngay lập tức
                System.err.println("Lỗi khi đồng bộ sản phẩm: " + item.getProductVariantId() + " - " + e.getMessage());
            }
        }

        return new ResponseMessageDTO(HttpStatus.OK, "Đồng bộ giỏ hàng thành công.");
    }
}
