package e_commerce.monolithic.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserUpdateAdminDTO {
    private int id; // để xác định user cần update
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private boolean enabled;
}
