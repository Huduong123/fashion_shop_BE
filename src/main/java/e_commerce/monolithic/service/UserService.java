package e_commerce.monolithic.service;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserResgisterDTO;
import e_commerce.monolithic.entity.User;

import java.util.Optional;

public interface UserService {
    boolean login(UserLoginDTO userLoginDTO);

    User register(UserResgisterDTO userResgisterDTO);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);
}
