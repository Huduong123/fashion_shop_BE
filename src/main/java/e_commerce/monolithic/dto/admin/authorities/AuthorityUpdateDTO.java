package e_commerce.monolithic.dto.admin.authorities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityUpdateDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "Quyền không được để trống")
    private String authority;
}
