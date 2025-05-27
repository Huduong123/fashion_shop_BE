package e_commerce.monolithic.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateAdminDTO {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private boolean enabled;

}
