package e_commerce.monolithic.service.common;

import e_commerce.monolithic.entity.User;

import java.util.Optional;

public interface UserValidationService {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}
