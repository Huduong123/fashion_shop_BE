package e_commerce.monolithic.dto.user.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItemResponseDTO {

    private Long productVariantId;
    private Long productId;
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;

    private int quantity;
    private BigDecimal price;

}
