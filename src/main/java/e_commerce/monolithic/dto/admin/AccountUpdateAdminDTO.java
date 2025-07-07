package e_commerce.monolithic.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AccountUpdateAdminDTO {
    private Long id; // để xác định user cần update
    @NotBlank(message = "email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Pattern(regexp = "^[\\w.+\\-]+@gmail\\.com$", message = "Email phải đúng định dạng @gmail.com")
    private String email;
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 6, message = "Họ và tên phải trên 5 ký tự")
    private String fullname;
    @NotBlank(message = "phone không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải đúng định dạng 0xxxxxxxxx")
    private String phone;
    @NotBlank(message = "giới tính không được để trống")
    private String gender;
    @NotNull(message = "ngày sinh không được để trống")
    @JsonProperty("birth_day")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;
    // Password is optional - only update if provided
    @Size(min = 6, message = "Password phải trên 5 ký tự")
    @Pattern(regexp = "^$|^[A-Z][^\\s]*[!@#$%^&*(),.?\":{}|<>]+[^\\s]*$", message = "Password phải bắt đầu bằng chữ hoa, có ít nhất 1 ký tự đặc biệt và không chứa khoảng trắng (hoặc để trống để không thay đổi)")
    private String password;
    private boolean enabled;
}
