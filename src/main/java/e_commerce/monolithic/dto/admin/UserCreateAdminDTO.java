package e_commerce.monolithic.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateAdminDTO {
    private String userName;
    private String passWord;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private boolean enabled;

}
