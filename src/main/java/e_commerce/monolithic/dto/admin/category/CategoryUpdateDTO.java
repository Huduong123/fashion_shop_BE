package e_commerce.monolithic.dto.admin.category;

import e_commerce.monolithic.entity.enums.CategoryStatus;
import e_commerce.monolithic.entity.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, message = "Tên danh mục phải trên 2 ký tự")
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    private String name;

    @Size(max = 120, message = "Slug không được vượt quá 120 ký tự")
    private String slug; // Optional - will be auto-generated from name if not provided

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự") // MỚI
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @NotNull(message = "Kiểu danh mục không được để trống")
    private CategoryType type;

    @NotNull(message = "Trạng thái danh mục không được để trống")
    private CategoryStatus status;

    // ID của category cha (null nếu là root category)
    private Long parentId;
}
