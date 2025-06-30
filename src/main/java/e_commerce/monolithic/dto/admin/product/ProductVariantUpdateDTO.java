package e_commerce.monolithic.dto.admin.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductVariantUpdateDTO {

    private Long id; // QUAN TRỌNG: Dùng để xác định biến thể cần cập nhật

    @NotNull(message = "Color ID không được để trống")
    private Long colorId;

    @NotNull(message = "Size ID không được để trống")
    private Long sizeId;

    @NotNull(message = "Giá tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tiền phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không thể âm")
    private Integer quantity;

    private String imageUrl;


}
