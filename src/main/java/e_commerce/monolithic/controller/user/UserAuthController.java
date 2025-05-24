package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.dto.auth.UserReponseDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.service.auth.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final UserMapper userMapper;

    public UserAuthController(UserAuthService userAuthService, UserMapper userMapper) {
        this.userAuthService = userAuthService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserReponseDTO> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        User user = userAuthService.register(userRegisterDTO);
        UserReponseDTO reponseDTO = userMapper.converToDTO(user);
        return ResponseEntity.ok(reponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token = userAuthService.login(userLoginDTO);
        return ResponseEntity.ok().body("Bearer " + token);
    }
}
