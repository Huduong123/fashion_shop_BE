package e_commerce.monolithic.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminDTO {
    private int id;
    private String userName;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
