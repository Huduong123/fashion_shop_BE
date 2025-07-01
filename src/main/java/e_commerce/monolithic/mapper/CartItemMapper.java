package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.user.cart_item.CartItemResponseDTO;
import e_commerce.monolithic.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {

    public CartItemResponseDTO convertToDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        var variant = cartItem.getProductVariant();
        var product = variant.getProduct();
        var color = variant.getColor();
        var size = variant.getSize();

        BigDecimal subTotal = variant.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .productVariantId(variant.getId())
                .productName(product.getName())
                .colorName(color.getName())
                .sizeName(size.getName())
                .imageUrl(variant.getImageUrl())
                .price(variant.getPrice())
                .quantity(cartItem.getQuantity())
                .subTotal(subTotal)
                .build();
    }
}
