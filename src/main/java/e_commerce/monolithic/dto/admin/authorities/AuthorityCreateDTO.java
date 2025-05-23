package e_commerce.monolithic.dto.admin.authorities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthorityCreateDTO {

    @NotNull(message = "Người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Quyền không được để trống")
    private String authority;
}
