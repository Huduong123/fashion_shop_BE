package e_commerce.monolithic.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserUpdateAdminDTO {
    private Long id; // để xác định user cần update
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private boolean enabled;
}
