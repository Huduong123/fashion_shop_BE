package e_commerce.monolithic.dto.user.cart_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemCreateDTO {

    @NotNull(message = "Product Variant Id cannot be null")
    private Long productVariantId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 1")
    private Integer quantity;
}
