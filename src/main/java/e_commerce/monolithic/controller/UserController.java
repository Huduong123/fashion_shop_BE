package e_commerce.monolithic.controller;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserResgisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserResgisterDTO userResgisterDTO) {
        if (userService.existsByUsername(userResgisterDTO.getUserName())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userService.existsByEmail(userResgisterDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (userService.existsByPhone(userResgisterDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Phone Number already exists");
        }
        User user = userService.register(userResgisterDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        boolean success = userService.login(userLoginDTO);
        if (success) {
            return ResponseEntity.ok("Login successful");
        }else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    public ResponseEntity<?> getUserByUserName(@PathVariable String username){
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
