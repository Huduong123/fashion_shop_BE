package e_commerce.monolithic.service;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;

import java.util.Optional;

public interface UserService {
    String login(UserLoginDTO userLoginDTO);

    User register(UserRegisterDTO userRegisterDTO);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean isPasswordMatch(String rawPassword, String encodedPassword);
}
