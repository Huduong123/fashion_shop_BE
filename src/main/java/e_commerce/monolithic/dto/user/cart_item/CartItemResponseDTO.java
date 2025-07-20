package e_commerce.monolithic.dto.user.cart_item;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDTO {
    private Long id;

    private Long productId;
    private Long productVariantId;
    private Long sizeId;

    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;
    private Integer stock;
}
