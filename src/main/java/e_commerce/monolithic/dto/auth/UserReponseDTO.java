package e_commerce.monolithic.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReponseDTO {
    Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private Set<String> roles;
}
