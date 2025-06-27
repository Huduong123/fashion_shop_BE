package e_commerce.monolithic.dto.admin.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;


}
