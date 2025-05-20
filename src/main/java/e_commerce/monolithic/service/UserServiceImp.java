package e_commerce.monolithic.service;

import java.time.LocalDateTime;
import java.util.Optional;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.repository.AuthorityRepository;
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
    private final AuthorityRepository authorityRepository;

    public UserServiceImp(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    @Transactional
    public String login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    @Override
    @Transactional
    public String loginAdmin(AdminLoginDTO adminLoginDTO) {
        User user = userRepository.findByUsername(adminLoginDTO.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(adminLoginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        // Kiểm tra user có ROLE_ADMIN không
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new IllegalArgumentException("Access denied. You a not an admin");
        }
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }
        return jwtUtil.generateToken(user.getUsername());
    }



    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        if (existsByUsername(userRegisterDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if  (existsByPhone(userRegisterDTO.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        // Thêm xác thực dữ liệu
        validateUserRegisterDTO(userRegisterDTO);

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setEmail(userRegisterDTO.getEmail());
        user.setFullname(userRegisterDTO.getFullname());
        user.setPhone(userRegisterDTO.getPhone());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser =  userRepository.save(user);

        // Tạo quyền mặc đinh cho ROLE_USER
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser( savedUser );
        authority.setCreatedAt(LocalDateTime.now());
        authority.setUpdatedAt(LocalDateTime.now());

        authorityRepository.save(authority);
        savedUser.getAuthorities().add(authority);

        return savedUser;
    }

    private void validateUserRegisterDTO (UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userRegisterDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
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
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}
