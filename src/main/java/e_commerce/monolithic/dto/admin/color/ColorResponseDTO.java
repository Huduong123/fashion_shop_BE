package e_commerce.monolithic.dto.admin.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorResponseDTO {

    private Long id;

    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
