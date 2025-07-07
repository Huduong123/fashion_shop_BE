package e_commerce.monolithic.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    // private BigDecimal price;
    // private int quantity;
    //
    // private String imageUrl;

    private Boolean enabled;

    private Long categoryId;
    private String categoryName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ProductVariantResponseDTO> productVariants;

    private Boolean canDelete;
}
