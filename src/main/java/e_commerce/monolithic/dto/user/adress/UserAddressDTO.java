package e_commerce.monolithic.dto.user.adress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {

    private Long id;
    private Long userId;
    private String recipientName;
    private String phoneNumber;
    private String addressDetail;
    private Boolean isDefault;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
