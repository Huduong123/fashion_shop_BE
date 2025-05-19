package e_commerce.monolithic.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.repository.UserRepository;
import e_commerce.monolithic.security.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public String login(UserLoginDTO userLoginDTO) {
        Optional<User> userOpt = userRepository.findByUsername(userLoginDTO.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword()) && user.isEnabled()) {
                return jwtUtil.generateToken(user.getUsername());
            }

        }
        return null;
    }

    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setEmail(userRegisterDTO.getEmail());
        user.setFullname(userRegisterDTO.getFullname());
        user.setPhone(userRegisterDTO.getPhone());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Force load authorities
            user.getAuthorities().size();
        }
        return userOpt;
    }

    @Override
    @Transactional
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
