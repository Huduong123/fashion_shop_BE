package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemCreateDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemResponseDTO;
import java.util.List;

public interface CartService {

    // lấy tất cả sản phẩm
    List<CartItemResponseDTO> getCartItems(String username);

    // lấy 5 sản phẩm gần nhất
    List<CartItemResponseDTO> getRecentCartItems(String username, int limit);

    // thêm sản phẩm
    CartItemResponseDTO addProductToCart(String username, CartItemCreateDTO cartItemCreateDTO);

    // tăng số lượng
    CartItemResponseDTO increaseItemQuantity(String username, Long cartItemId);

    // giảm số lượng
    CartItemResponseDTO decreaseItemQuantity(String username, Long cartItemId);

    // xóa sản phẩm
    ResponseMessageDTO removeCartItem(String username, Long cartItemId);


    ResponseMessageDTO clearCart (String username);

    /**
 * Đồng bộ hóa một danh sách các sản phẩm (từ localStorage của khách)
 * vào giỏ hàng của người dùng đã đăng nhập.
 * @param username Tên người dùng.
 * @param itemsToSync Danh sách các sản phẩm cần đồng bộ.
 * @return ResponseMessageDTO cho biết kết quả.
 */
ResponseMessageDTO syncCart(String username, List<CartItemCreateDTO> itemsToSync);
}
