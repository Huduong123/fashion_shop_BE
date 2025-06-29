package e_commerce.monolithic.dto.admin.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 3, message = "Tên danh mục phải trên 3 ký tự")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên danh mục phải chứa ít nhất một ký tự chữ")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự") // MỚI
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Mô tả phải chứa ít nhất một ký tự chữ")
    private String description;


}
