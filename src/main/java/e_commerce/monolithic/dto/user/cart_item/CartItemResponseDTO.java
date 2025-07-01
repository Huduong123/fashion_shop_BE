package e_commerce.monolithic.dto.user.cart_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDTO {
    private Long id;

    private Long productVariantId;

    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private BigDecimal price;
    private    int quantity;
    private BigDecimal subTotal;

}
