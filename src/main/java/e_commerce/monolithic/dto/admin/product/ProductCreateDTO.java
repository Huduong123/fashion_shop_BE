package e_commerce.monolithic.dto.admin.product;

import e_commerce.monolithic.entity.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, message = "Tên sản phẩm phải có ít nhất 3 ký tự")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên sản phẩm phải chứa ít nhất một ký tự chữ")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự") // MỚI
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Mô tả phải chứa ít nhất một ký tự chữ")
    private String description;

    @NotNull(message = "Giá tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tiền phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không thể âm")
    private Integer quantity;

    private String imageUrl;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean enabled;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}
