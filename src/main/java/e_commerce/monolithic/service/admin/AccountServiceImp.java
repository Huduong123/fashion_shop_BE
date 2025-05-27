package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.UserAdminDTO;
import e_commerce.monolithic.dto.admin.UserCreateAdminDTO;
import e_commerce.monolithic.dto.admin.UserUpdateAdminDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.repository.UserRepository;
import e_commerce.monolithic.service.common.UserValidationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger; // Thêm import này
import org.slf4j.LoggerFactory; // Thêm import này
@Service
public class AccountServiceImp implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    public AccountServiceImp(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    @Override
    public List<UserAdminDTO> getAllAccount() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserAdminDTO createAccount(UserCreateAdminDTO userCreateAdminDTO) {
        User newUser = new User();
        newUser.setUsername(userCreateAdminDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userCreateAdminDTO.getPassword()));
        newUser.setEmail(userCreateAdminDTO.getEmail());
        newUser.setPhone(userCreateAdminDTO.getPhone());

        newUser.setEnabled(userCreateAdminDTO.isEnabled());

        User savedUser = userRepository.save(newUser);
        logger.info("Account created successfully with ID: {} for Username: {}", savedUser.getId(), savedUser.getUsername());
        return userMapper.convertToAdminDTO(savedUser);
    }

    private void validateNewAccountCreate(UserCreateAdminDTO dto){
        if (userValidationService.existsByUsername(dto.getUsername())){
            logger.warn("Username {} already exists!",  dto.getUsername());
            throw new IllegalArgumentException("Username " + dto.getUsername() + " already exists");
        }

        if (userValidationService.existsByEmail(dto.getEmail())){
            logger.warn("Email {} already exists!",  dto.getEmail());
            throw new IllegalArgumentException("Email " + dto.getEmail() + " already exists");
        }


    }

    @Override
    public UserAdminDTO updateAccount(UserUpdateAdminDTO userUpdateAdminDTO) {
        return null;
    }

    @Override
    public void deleteAccount(Long userId) {

    }

    @Override
    public UserAdminDTO getAccountById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: " + id);
                    return new NotFoundException("Account not found with ID: " + id);
                });
        return userMapper.convertToAdminDTO(user);
    }
}
