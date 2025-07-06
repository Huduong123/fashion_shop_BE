package e_commerce.monolithic.dto.admin.color;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorCreateDTO {

    @NotBlank(message = "Tên màu sắc không đuợc để trống")
    @Size(min = 1, max = 50, message = "Tên màu sắc phải có từ 1 đến 50 ký tự")
    @Pattern(regexp = ".*[\\p{L}]+.*", message = "Tên màu sắc phải chứa ít nhất một ký tự chữ")
    private String name;
}
