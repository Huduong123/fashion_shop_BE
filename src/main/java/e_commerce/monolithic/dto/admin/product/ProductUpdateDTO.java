package e_commerce.monolithic.dto.admin.product;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Sửa lại class ProductUpdateDTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, message = "Tên sản phẩm phải có ít nhất 3 ký tự") // Sửa @Min thành @Size cho đúng
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên sản phẩm phải chứa ít nhất một ký tự chữ") // <-- THÊM VÀO ĐÂY
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự") // <-- THÊM VÀO ĐÂY
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Mô tả phải chứa ít nhất một ký tự chữ") // <-- THÊM VÀO ĐÂY
    private String description;



    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean enabled;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    @Valid
    @Size(min = 1, message = "Sản phẩm phải có ít nhất một biến thể")
    private List<ProductVariantUpdateDTO> productVariants;
}
