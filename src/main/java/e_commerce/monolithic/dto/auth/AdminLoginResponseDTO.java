package e_commerce.monolithic.dto.auth;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponseDTO {
    private String token;
    private String username;
    private String message;
    private List<String> roles;





}