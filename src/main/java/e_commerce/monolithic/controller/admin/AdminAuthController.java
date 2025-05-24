package e_commerce.monolithic.controller.admin;


import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.security.JwtUtil;
import e_commerce.monolithic.service.auth.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> loginAdmin(@RequestBody @Valid AdminLoginDTO adminLoginDTO) {

        String token = userAuthService.loginAdmin(adminLoginDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }

}
