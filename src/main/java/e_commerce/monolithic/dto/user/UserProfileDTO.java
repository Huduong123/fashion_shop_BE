package e_commerce.monolithic.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserProfileDTO {
    private int id;
    private String userName;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private boolean enabled;
}
