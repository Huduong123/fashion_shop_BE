package e_commerce.monolithic.dto.admin.size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeResponseDTO {
    private Long id;

    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
