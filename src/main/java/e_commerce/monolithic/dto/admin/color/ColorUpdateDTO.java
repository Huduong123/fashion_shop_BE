package e_commerce.monolithic.dto.admin.color;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorUpdateDTO {
    @NotBlank(message = "Tên màu sắc không đuợc để trống")
    @Size(min = 3, message = "Tên màu sắc phải có ít nhất 3 kí tự")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên màu sắc phải chứa ít nhất một ký tự chữ")
    private String name;
}
