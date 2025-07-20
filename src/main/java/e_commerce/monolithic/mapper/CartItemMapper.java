package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.user.cart_item.CartItemResponseDTO;
import e_commerce.monolithic.entity.CartItem;
import e_commerce.monolithic.entity.ProductVariantSize;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemResponseDTO convertToDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        var variant = cartItem.getProductVariant();
        var product = variant.getProduct();
        var color = variant.getColor();
        var size = cartItem.getSize();

        // Find the specific ProductVariantSize for this size
        Optional<ProductVariantSize> productVariantSize = variant.getProductVariantSizes().stream()
                .filter(pvs -> pvs.getSize().getId().equals(size.getId()))
                .findFirst();

        BigDecimal price = productVariantSize.map(ProductVariantSize::getPrice)
                .orElse(BigDecimal.ZERO);
        Integer stock = productVariantSize.map(ProductVariantSize::getQuantity).orElse(0);
        BigDecimal subTotal = price.multiply(new BigDecimal(cartItem.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productVariantId(variant.getId())
                .sizeId(size.getId())
                .productName(product.getName())
                .colorName(color.getName())
                .sizeName(size.getName())
                .imageUrl(variant.getPrimaryImageUrl())
                .price(price)
                .quantity(cartItem.getQuantity())
                .subTotal(subTotal)
                .stock(stock)
                .build();
    }
}
