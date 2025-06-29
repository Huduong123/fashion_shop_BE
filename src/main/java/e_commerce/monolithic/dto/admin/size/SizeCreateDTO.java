package e_commerce.monolithic.dto.admin.size;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeCreateDTO {
    @NotBlank(message = "Tên kích cỡ không đuợc để trống")
    @Size(min = 3, message = "Tên kích cỡ phải có ít nhất 3 kí tự")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Tên kích cỡ phải chứa ít nhất một ký tự chữ")
    private String name;
}
