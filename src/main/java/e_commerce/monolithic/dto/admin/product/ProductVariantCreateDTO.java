package e_commerce.monolithic.dto.admin.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductVariantCreateDTO {

    @NotNull(message = "Color không được để trống")
    private Long colorId;

    private Long sizeId;

    @NotNull(message = "Giá tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tiền phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không thể âm")
    private Integer quantity;

    private String imageUrl; // Keep for backward compatibility

    private ProductVariantStatus status = ProductVariantStatus.ACTIVE;
    
    // List of variant images
    @Valid
    private List<ProductVariantImageDTO> images = new ArrayList<>();
}
