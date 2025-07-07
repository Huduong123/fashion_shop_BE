package e_commerce.monolithic.dto.admin;

import e_commerce.monolithic.dto.admin.authorities.AuthorityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private boolean enabled;
    private List<AuthorityDTO> authorities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
