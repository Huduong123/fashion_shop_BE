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
        Optional<User> userOpt = userService.findByUsername(adminLoginDTO.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if(!isAdmin){
                return ResponseEntity.status(403).body("Access Denied. Not an admin");
            }
            if(userService.isPasswordMatch(adminLoginDTO.getPassword(), user.getPassword()) && user.isEnabled()) {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok().body(token);
            }else {
                return  ResponseEntity.status(401).body("Invalid credentials");
            }
        }
        else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
