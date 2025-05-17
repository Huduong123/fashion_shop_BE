package e_commerce.monolithic.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateAdminDTO {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private boolean enabled;

}
