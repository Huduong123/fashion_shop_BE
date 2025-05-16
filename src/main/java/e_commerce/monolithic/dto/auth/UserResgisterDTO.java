package e_commerce.monolithic.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResgisterDTO {
    private String userName;
    private String passWord;
    private String email;
    private String fullName;
    private String phone;
}
