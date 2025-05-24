package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.UserChangePasswordDTO;
import e_commerce.monolithic.dto.user.UserProfileDTO;
import e_commerce.monolithic.dto.user.UserUpdateProfileDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.repository.UserRepository;
import e_commerce.monolithic.service.admin.AuthorityServiceImp;
import e_commerce.monolithic.service.common.UserValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    public UserServiceImp(UserRepository userRepository, UserMapper userMapper,  PasswordEncoder passwordEncoder, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
    }

    @Override
    public UserProfileDTO getUserprofile(String username) {
        User user = userValidationService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return  userMapper.convertToUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO updateUserProfile(String username, UserUpdateProfileDTO userUpdateProfileDTO) {
        User user = userValidationService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        validateEmail(userUpdateProfileDTO,user);
        validatePhone(userUpdateProfileDTO,user);

        userMapper.updateUserFromDTO(user, userUpdateProfileDTO);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return userMapper.convertToUserProfileDTO(updatedUser);
    }

    private void validateEmail(UserUpdateProfileDTO userUpdateProfileDTO, User user) {
        // Kiểm tra email:
        // 1. Nếu email mới được cung cấp (khác null)
        // 2. Và email mới khác email hiện tại của người dùng
        // 3. Và email mới đã tồn tại trong hệ thống (của người dùng khác)
        if (userUpdateProfileDTO.getEmail() != null &&
                !userUpdateProfileDTO.getEmail().equals(user.getEmail()) && // Đảm bảo email mới khác email cũ
                userValidationService.existsByEmail(userUpdateProfileDTO.getEmail())) {
            logger.info("Email already exists with username: " + user.getUsername());
            throw new IllegalArgumentException("Email already exists");
        }
    }
    private void validatePhone(UserUpdateProfileDTO userUpdateProfileDTO, User user) {
        // Kiểm tra phone:
        // 1. Nếu phone mới được cung cấp (khác null)
        // 2. Và phone mới khác phone hiện tại của người dùng
        // 3. Và phone mới đã tồn tại trong hệ thống (của người dùng khác)
        if (userUpdateProfileDTO.getPhone() != null &&
                !userUpdateProfileDTO.getPhone().equals(user.getPhone()) && // Đảm bảo phone mới khác phone cũ
                userValidationService.existsByPhone(userUpdateProfileDTO.getPhone())) {
            logger.info("Phone already exists with username: " + user.getUsername());
            throw new IllegalArgumentException("Phone already exists");
        }
    }
    @Override
    public ResponseMessageDTO changePassword(String username, UserChangePasswordDTO userChangePasswordDTO) {
        User user = userValidationService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        validatePassword(username, userChangePasswordDTO, user);

        String encodedNewPassword = passwordEncoder.encode(userChangePasswordDTO.getNewPassword());
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return  new ResponseMessageDTO(HttpStatus.OK, "Mật khẩu đã được cập nhật thành công");
    }
        // check new password is match old password

    private void validatePassword(String username, UserChangePasswordDTO userChangePasswordDTO, User user) {
        if (!userValidationService.isPasswordMatch(userChangePasswordDTO.getOldPassword(), user.getPassword())) {
            logger.error("Passwords do not match");
            throw new IllegalArgumentException("Old password doesn't match");
        }
        if (!userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getConfirmNewPassword())){
            logger.error("New password and confirm new password do not match");
            throw new IllegalArgumentException("New password and confirm new password do not match");
        }
        if(userValidationService.isPasswordMatch(userChangePasswordDTO.getNewPassword(), user.getPassword())){
            logger.error("New password cannot be the same as the old password");
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }
    }
}
