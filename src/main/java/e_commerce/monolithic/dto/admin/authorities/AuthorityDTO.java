package e_commerce.monolithic.dto.admin.authorities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {
    private Long id;
    private String authority;
    private String username;
}
