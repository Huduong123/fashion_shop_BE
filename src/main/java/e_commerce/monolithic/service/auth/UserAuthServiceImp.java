package e_commerce.monolithic.service.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserMapper;
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
public class UserAuthServiceImp implements UserAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    public UserAuthServiceImp(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
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

        // Tạo quyền mặc định ROLE_USER
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setCreatedAt(LocalDateTime.now());
        authority.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.convertToEntity(
                userRegisterDTO,
                Set.of(authority),
                passwordEncoder.encode(userRegisterDTO.getPassword())
        );

        //Thiết lập quan hệ 2 chiều giữa User và Authority
        authority.setUser(user);

        //Lưu user và authority vào DB
        User savedUser = userRepository.save(user);
        authorityRepository.save(authority);

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
