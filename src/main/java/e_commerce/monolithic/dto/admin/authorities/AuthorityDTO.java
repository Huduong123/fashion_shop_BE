package e_commerce.monolithic.dto.admin.authorities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    private Long id;
    private String name;
    
    public AuthorityDTO(String name) {
        this.name = name;
    }
}
