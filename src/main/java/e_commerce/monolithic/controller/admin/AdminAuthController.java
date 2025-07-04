package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.dto.auth.AdminLoginResponseDTO;
import e_commerce.monolithic.security.JwtUtil;
import e_commerce.monolithic.service.auth.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final UserAuthService userAuthService;

    private final JwtUtil jwtUtil;

    @Autowired
    public AdminAuthController(UserAuthService userAuthService, JwtUtil jwtUtil) {
        this.userAuthService = userAuthService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDTO> loginAdmin(@RequestBody @Valid AdminLoginDTO adminLoginDTO) {
        try {
            String token = userAuthService.loginAdmin(adminLoginDTO);

            AdminLoginResponseDTO response = new AdminLoginResponseDTO(
                    token,
                    adminLoginDTO.getUsername(),
                    "Login successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Validate token using JwtUtil
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);

                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("username", username);
                    response.put("message", "Token is valid");

                    return ResponseEntity.ok(response);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Invalid or expired token");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Token verification failed");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
