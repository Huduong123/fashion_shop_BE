package e_commerce.monolithic.controller.admin;


import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.security.JwtUtil;
import e_commerce.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public AdminAuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminLoginDTO adminLoginDTO) {

        String token = userService.loginAdmin(adminLoginDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }

}
