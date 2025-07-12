package e_commerce.monolithic.dto.admin.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantSizeResponseDTO {

    private Long id;
    private Long sizeId;
    private String sizeName;
    private BigDecimal price;
    private Integer quantity;
    private boolean available; // Calculated field: quantity > 0
} 