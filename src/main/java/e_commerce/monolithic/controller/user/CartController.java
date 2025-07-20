package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemCreateDTO;
import e_commerce.monolithic.dto.user.cart_item.CartItemResponseDTO;
import e_commerce.monolithic.service.user.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * API 1: Lấy tất cả sản phẩm trong giỏ hàng của người dùng hiện tại.
     */
    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> getCart(Principal principal) {
        List<CartItemResponseDTO> cartItems = cartService.getCartItems(principal.getName());
        return ResponseEntity.ok(cartItems);
    }

    /**
     * API 2: Lấy 5 sản phẩm thêm vào giỏ hàng gần nhất.
     */
    @GetMapping("/recent")
    public ResponseEntity<List<CartItemResponseDTO>> getRecentCartItems(Principal principal) {
        // Lấy 5 sản phẩm gần nhất
        List<CartItemResponseDTO> recentItems = cartService.getRecentCartItems(principal.getName(), 5);
        return ResponseEntity.ok(recentItems);
    }

    /**
     * API 3: Thêm một sản phẩm vào giỏ hàng.
     */
    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addProductToCart(
            @Valid @RequestBody CartItemCreateDTO createDTO,
            Principal principal) {
        CartItemResponseDTO newCartItem = cartService.addProductToCart(principal.getName(), createDTO);
        return new ResponseEntity<>(newCartItem, HttpStatus.CREATED);
    }

    /**
     * API 4: Tăng số lượng của một sản phẩm trong giỏ hàng.
     */
    @PutMapping("/{cartItemId}/increase")
    public ResponseEntity<CartItemResponseDTO> increaseQuantity(
            @PathVariable Long cartItemId,
            Principal principal) {
        CartItemResponseDTO updatedItem = cartService.increaseItemQuantity(principal.getName(), cartItemId);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * API 5: Giảm số lượng của một sản phẩm trong giỏ hàng.
     */
    @PutMapping("/{cartItemId}/decrease")
    public ResponseEntity<CartItemResponseDTO> decreaseQuantity(
            @PathVariable Long cartItemId,
            Principal principal) {
        CartItemResponseDTO updatedItem = cartService.decreaseItemQuantity(principal.getName(), cartItemId);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * API 6: Xóa một sản phẩm khỏi giỏ hàng.
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ResponseMessageDTO> removeCartItem(
            @PathVariable Long cartItemId,
            Principal principal) {
        ResponseMessageDTO responseMessageDTO =  cartService.removeCartItem(principal.getName(), cartItemId);
        return ResponseEntity.ok(responseMessageDTO); // HTTP 204 No Content
    }

    /**
     * API 7: Xóa tất cả sản phẩm khỏi giỏ hàng (dọn dẹp giỏ hàng).
     */
    @DeleteMapping
    public ResponseEntity<ResponseMessageDTO> clearCart(Principal principal) {
        ResponseMessageDTO response = cartService.clearCart(principal.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * API 8: Đồng bộ giỏ hàng từ localStorage của khách vào giỏ hàng của người dùng.
     */
    @PostMapping("/sync")
    public ResponseEntity<ResponseMessageDTO> syncCart(
            @RequestBody List<CartItemCreateDTO> itemsToSync,
            Principal principal) {
        ResponseMessageDTO response = cartService.syncCart(principal.getName(), itemsToSync);
        return ResponseEntity.ok(response);
    }
}
