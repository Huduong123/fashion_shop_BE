package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.AccountAdminDTO;
import e_commerce.monolithic.dto.admin.AccountCreateAdminDTO;
import e_commerce.monolithic.dto.admin.AccountUpdateAdminDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.repository.UserRepository;
import e_commerce.monolithic.service.common.UserValidationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger; // Thêm import này
import org.slf4j.LoggerFactory; // Thêm import này

@Service
public class AccountServiceImp implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    public AccountServiceImp(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
            UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountAdminDTO> getAllAccount() {
        List<User> users = userRepository.findAllWithAuthorities();
        return users.stream()
                .map(userMapper::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountAdminDTO createAccount(AccountCreateAdminDTO accountCreateAdminDTO) {
        validateNewAccountCreate(accountCreateAdminDTO);

        User newUser = new User();
        newUser.setUsername(accountCreateAdminDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(accountCreateAdminDTO.getPassword()));
        newUser.setEmail(accountCreateAdminDTO.getEmail());
        newUser.setPhone(accountCreateAdminDTO.getPhone());
        newUser.setFullname(accountCreateAdminDTO.getFullname());
        newUser.setGender(accountCreateAdminDTO.getGender());
        newUser.setBirthDate(accountCreateAdminDTO.getBirthday());
        newUser.setEnabled(accountCreateAdminDTO.isEnabled());

        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser(newUser);

        newUser.getAuthorities().add(authority);

        User savedUser = userRepository.save(newUser);
        logger.info("Account created successfully with ID: {} for Username: {}", savedUser.getId(),
                savedUser.getUsername());
        return userMapper.convertToAdminDTO(savedUser);
    }

    private void validateNewAccountCreate(AccountCreateAdminDTO dto) {
        if (userValidationService.existsByUsername(dto.getUsername())) {
            logger.warn("Username {} already exists!", dto.getUsername());
            throw new IllegalArgumentException("Username " + dto.getUsername() + " already exists");
        }

        if (userValidationService.existsByEmail(dto.getEmail())) {
            logger.warn("Email {} already exists!", dto.getEmail());
            throw new IllegalArgumentException("Email " + dto.getEmail() + " already exists");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()
                && userValidationService.existsByPhone(dto.getPhone())) {
            logger.warn("Phone {} already exists!", dto.getPhone());
            throw new IllegalArgumentException("Phone " + dto.getPhone() + " already exists");
        }
        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Email {} is not valid!", dto.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }
        if (dto.getPassword().length() < 8) {
            logger.warn("Password length must be at least 8 characters");
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }
    }

    @Override
    @Transactional
    public AccountAdminDTO updateAccount(AccountUpdateAdminDTO accountUpdateAdminDTO) {
        User existingUser = userRepository.findByIdWithAuthorities(accountUpdateAdminDTO.getId())
                .orElseThrow(() -> {
                    logger.warn("User not found with Id {} for update", accountUpdateAdminDTO.getId());
                    return new NotFoundException("User not found with ID: " + accountUpdateAdminDTO.getId());
                });

        validateNewAccountUpdate(accountUpdateAdminDTO, existingUser);

        // Handle password update if provided
        if (accountUpdateAdminDTO.getPassword() != null && !accountUpdateAdminDTO.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(accountUpdateAdminDTO.getPassword()));
            logger.info("Password updated for user ID: {}", existingUser.getId());
        }

        userMapper.updateAccountFromDTO(existingUser, accountUpdateAdminDTO);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.convertToAdminDTO(updatedUser);
    }

    private void validateNewAccountUpdate(AccountUpdateAdminDTO dto, User existingUser) {
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            if (userValidationService.existsByEmail(dto.getEmail())) {
                logger.warn("Email {} already exists!", dto.getEmail());
                throw new IllegalArgumentException("Email " + dto.getEmail() + " already exists");
            }
        }
        if (dto.getPhone() != null && !dto.getPhone().equalsIgnoreCase(existingUser.getPhone())) {
            if (userValidationService.existsByPhone(dto.getPhone())) {
                logger.warn("Phone {} already exists!", dto.getPhone());
                throw new IllegalArgumentException("Phone " + dto.getPhone() + " already exists");
            }
        }

    }

    @Override
    @Transactional
    public void deleteAccount(Long userId) {
        // validate
        if (!userRepository.existsById(userId)) {
            logger.warn("Attempt to delete non-existent account with Id: {}", userId);
            throw new NotFoundException("Account not found with Id: " + userId);
        }
        // xóa account
        userRepository.deleteById(userId);
        logger.info("Account deleted successfully with ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountAdminDTO getAccountById(Long id) {
        User user = userRepository.findByIdWithAuthorities(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: " + id);
                    return new NotFoundException("Account not found with ID: " + id);
                });
        return userMapper.convertToAdminDTO(user);
    }

    // TRIỂN KHAI PHƯƠNG THỨC TÌM KIẾM
    @Override
    @Transactional(readOnly = true)
    public List<AccountAdminDTO> searchAccounts(
            String username,
            String email,
            String fullname,
            String phone,
            String gender,
            LocalDate birthday,
            Boolean enabled,
            String role,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime updatedAtStart,
            LocalDateTime updatedAtEnd) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }
            if (fullname != null && !fullname.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullname")),
                        "%" + fullname.toLowerCase() + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%")); // Phone có thể không cần
                                                                                            // lower()
            }
            if (gender != null && !gender.isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("gender")), gender.toLowerCase()));
            }
            if (birthday != null) {
                predicates.add(criteriaBuilder.equal(root.get("birthDate"), birthday));
            }
            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), enabled));
            }

            // Tìm kiếm theo vai trò (role)
            if (role != null && !role.isEmpty()) {
                String searchRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.upper(root.join("authorities").get("authority")),
                        "%" + searchRole.toUpperCase() + "%"));
            }

            // Tìm kiếm theo khoảng thời gian tạo/cập nhật
            if (createdAtStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtStart));
            }
            if (createdAtEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtEnd));
            }
            if (updatedAtStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), updatedAtStart));
            }
            if (updatedAtEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), updatedAtEnd));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<User> users = userRepository.findAll(spec);
        logger.info("Found {} accounts matching search criteria.", users.size());
        return users.stream()
                .map(userMapper::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountAdminDTO toggleAccountStatus(Long accountId, Boolean enabled) {
        User user = userRepository.findByIdWithAuthorities(accountId)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", accountId);
                    return new NotFoundException("Account not found with ID: " + accountId);
                });

        user.setEnabled(enabled);
        User updatedUser = userRepository.save(user);

        logger.info("Account {} status changed to: {}", accountId, enabled ? "enabled" : "disabled");
        return userMapper.convertToAdminDTO(updatedUser);
    }

    @Override
    @Transactional
    public AccountAdminDTO addRoleToUser(Long userId, String roleName) {
        User user = userRepository.findByIdWithAuthorities(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", userId);
                    return new NotFoundException("Account not found with ID: " + userId);
                });

        // Validate role name
        final String finalRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;

        // Check if user already has this role
        boolean hasRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(finalRoleName));

        if (hasRole) {
            logger.warn("User {} already has role: {}", userId, finalRoleName);
            throw new IllegalArgumentException("User already has role: " + finalRoleName);
        }

        // Add new role
        Authority authority = new Authority();
        authority.setAuthority(finalRoleName);
        authority.setUser(user);
        user.getAuthorities().add(authority);

        User updatedUser = userRepository.save(user);
        logger.info("Role {} added to user {}", finalRoleName, userId);
        return userMapper.convertToAdminDTO(updatedUser);
    }

    @Override
    @Transactional
    public AccountAdminDTO removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findByIdWithAuthorities(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", userId);
                    return new NotFoundException("Account not found with ID: " + userId);
                });

        // Validate role name
        final String finalRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;

        // Find the authority to remove
        Authority authorityToRemove = user.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().equals(finalRoleName))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("User {} does not have role: {}", userId, finalRoleName);
                    return new IllegalArgumentException("User does not have role: " + finalRoleName);
                });

        // Check if user has at least one role left (prevent removing all roles)
        if (user.getAuthorities().size() <= 1) {
            logger.warn("Cannot remove the last role from user {}", userId);
            throw new IllegalArgumentException(
                    "Cannot remove the last role from user. User must have at least one role.");
        }

        // Remove the role
        user.getAuthorities().remove(authorityToRemove);

        User updatedUser = userRepository.save(user);
        logger.info("Role {} removed from user {}", finalRoleName, userId);
        return userMapper.convertToAdminDTO(updatedUser);
    }
}
