package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.dto.auth.UserReponseDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAuthController {

    private final  UserService userService;
    private final UserMapper userMapper;

    public UserAuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserReponseDTO> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        User user = userService.register(userRegisterDTO);
        UserReponseDTO reponseDTO = userMapper.converToDTO(user);
        return ResponseEntity.ok(reponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }



    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String username){
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
