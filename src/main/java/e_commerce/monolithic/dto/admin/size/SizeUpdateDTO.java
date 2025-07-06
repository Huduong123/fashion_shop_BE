package e_commerce.monolithic.dto.admin.size;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeUpdateDTO {
    @NotBlank(message = "Tên kích cỡ không được để trống")
    @Size(min = 1, max = 20, message = "Tên kích cỡ phải có từ 1 đến 20 ký tự")
    private String name;
}
