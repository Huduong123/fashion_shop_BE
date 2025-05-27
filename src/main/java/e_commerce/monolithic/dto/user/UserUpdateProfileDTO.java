package e_commerce.monolithic.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateProfileDTO {
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
}
