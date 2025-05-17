package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAuthController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userService.existsByUsername(userRegisterDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userService.existsByEmail(userRegisterDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (userService.existsByPhone(userRegisterDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone Number already exists");
        }
        User user = userService.register(userRegisterDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        if (token != null) {
            return ResponseEntity.ok().body("Bearer " + token);
        }else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String username){
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
