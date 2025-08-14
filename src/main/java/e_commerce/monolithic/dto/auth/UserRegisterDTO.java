package e_commerce.monolithic.dto.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRegisterDTO {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 6, message = "Username phải trên 5 ký tự")
    @Pattern(regexp = "^\\S+$", message = "Username không được chứa khoảng trắng")
    private String username;
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải trên 5 ký tự")
    @Pattern(regexp = "^[A-Z][^\\s]*[!@#$%^&*(),.?\":{}|<>]+[^\\s]*$", message = "Password phải bắt đầu bằng chữ hoa, có ít nhất 1 ký tự đặc biệt và không chứa khoảng trắng")
    private String password;
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

    private String gender;

    private LocalDate birthDate;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
