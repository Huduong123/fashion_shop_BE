package e_commerce.monolithic.dto.user.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItemResponseDTO {

    private Long productVariantId;
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;

    private int quantity;
    private BigDecimal price;

}
