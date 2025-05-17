package e_commerce.monolithic.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserProfileDTO {
    private int id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private boolean enabled;
}
