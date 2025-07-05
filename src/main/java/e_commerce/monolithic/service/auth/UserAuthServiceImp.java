package e_commerce.monolithic.service.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.exeption.AuthenticationFailedException;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.repository.AuthorityRepository;
import e_commerce.monolithic.service.admin.AuthorityServiceImp;
import e_commerce.monolithic.service.common.UserValidationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.repository.UserRepository;
import e_commerce.monolithic.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserAuthServiceImp implements UserAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImp.class);

    public UserAuthServiceImp(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository, UserMapper userMapper,
            UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
    }

    @Override
    @Transactional
    public String login(UserLoginDTO userLoginDTO) {
        User user = userValidationService.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found with username: " + userLoginDTO.getUsername());
                    return new EntityNotFoundException("User not found with username: " + userLoginDTO.getUsername());
                });

        validatePasswordInvalid(userLoginDTO.getPassword(), user);
        validateEnabled(user);

        return jwtUtil.generateToken(user.getUsername());
    }

    @Override
    @Transactional
    public String loginAdmin(AdminLoginDTO adminLoginDTO) {
        // Tìm user, nếu không tồn tại thì ném AuthenticationFailedException
        Optional<User> userOptional = userValidationService.findByUsername(adminLoginDTO.getUsername());
        if (userOptional.isEmpty()) {
            throw new AuthenticationFailedException();
        }

        User user = userOptional.get();

        // Validate password - nếu sai cũng ném AuthenticationFailedException
        try {
            validatePasswordInvalid(adminLoginDTO.getPassword(), user);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationFailedException();
        }

        // Validate other conditions
        checkIsAdmin(user);
        validateEnabled(user);

        return jwtUtil.generateToken(user.getUsername());
    }

    private void validateEnabled(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }
    }

    private void checkIsAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));

        if (!isAdmin) {
            throw new IllegalArgumentException("Access denied. You are not an admin");
        }
    }

    private void validatePasswordInvalid(String rawPassword, User user) {
        if (!userValidationService.isPasswordMatch(rawPassword, user.getPassword())) { // Sử dụng service chung
            throw new IllegalArgumentException("Invalid password");
        }
    }

    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        if (userValidationService.existsByUsername(userRegisterDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userValidationService.existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userValidationService.existsByPhone(userRegisterDTO.getPhone())) {
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
                passwordEncoder.encode(userRegisterDTO.getPassword()));

        // Thiết lập quan hệ 2 chiều giữa User và Authority
        authority.setUser(user);

        // Lưu user và authority vào DB
        User savedUser = userRepository.save(user);
        authorityRepository.save(authority);

        return savedUser;
    }

    private void validateUserRegisterDTO(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userRegisterDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }

}
