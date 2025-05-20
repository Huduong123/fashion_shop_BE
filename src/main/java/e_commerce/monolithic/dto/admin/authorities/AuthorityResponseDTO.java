package e_commerce.monolithic.dto.admin.authorities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityResponseDTO {
    private Long id;
    private String username;
    private String authority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
