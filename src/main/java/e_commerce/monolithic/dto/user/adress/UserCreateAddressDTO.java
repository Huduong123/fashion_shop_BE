package e_commerce.monolithic.dto.user.adress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateAddressDTO {

    @NotBlank(message = "Tên người nhận không được để trống")
    private String recipientName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải đúng định dạng 10 số bắt đầu bằng 0")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String addressDetail;

    private Boolean isDefault;

}
