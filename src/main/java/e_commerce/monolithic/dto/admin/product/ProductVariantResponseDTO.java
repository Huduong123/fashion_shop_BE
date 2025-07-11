package e_commerce.monolithic.dto.admin.product;

import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductVariantResponseDTO {

    private Long id;

    private Long colorId;
    private String colorName;

    private Long sizeId;
    private String sizeName;

    private BigDecimal price;
    private int quantity;
    private String imageUrl; // Keep for backward compatibility
    private ProductVariantStatus status;

    // List of variant images
    private List<ProductVariantImageDTO> images = new ArrayList<>();

}
