package e_commerce.monolithic.dto.admin.product;

import java.util.ArrayList;
import java.util.List;

import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductVariantCreateDTO {

    @NotNull(message = "Color không được để trống")
    private Long colorId;

    @NotNull(message = "Danh sách size không được để trống")
    @Size(min = 1, message = "Biến thể phải có ít nhất 1 size")
    @Valid
    private List<ProductVariantSizeCreateDTO> sizes = new ArrayList<>();

    private String imageUrl; // Keep for backward compatibility

    private ProductVariantStatus status = ProductVariantStatus.ACTIVE;
    
    // List of variant images
    @Valid
    private List<ProductVariantImageDTO> images = new ArrayList<>();
}
