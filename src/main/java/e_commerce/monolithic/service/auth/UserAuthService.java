package e_commerce.monolithic.service.auth;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;

import java.util.Optional;

public interface UserAuthService {
    String login(UserLoginDTO userLoginDTO);
    String loginAdmin(AdminLoginDTO adminLoginDTO);
    User register(UserRegisterDTO userRegisterDTO);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean isPasswordMatch(String rawPassword, String encodedPassword);
}
