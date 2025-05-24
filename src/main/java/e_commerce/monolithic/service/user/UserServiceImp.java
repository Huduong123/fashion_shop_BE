package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.user.UserProfileDTO;
import e_commerce.monolithic.dto.user.UserUpdateProfileDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserMapper;
import e_commerce.monolithic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserProfileDTO getUserprofile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return  userMapper.convertToUserProfileDTO(user);
    }

    @Override
    public UserProfileDTO updateUserProfile(String username, UserUpdateProfileDTO userUpdateProfileDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        // Kiểm tra email:
        // 1. Nếu email mới được cung cấp (khác null)
        // 2. Và email mới khác email hiện tại của người dùng
        // 3. Và email mới đã tồn tại trong hệ thống (của người dùng khác)
        if (userUpdateProfileDTO.getEmail() != null &&
                !userUpdateProfileDTO.getEmail().equals(user.getEmail()) && // Đảm bảo email mới khác email cũ
                userRepository.existsByEmail(userUpdateProfileDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Kiểm tra phone:
        // 1. Nếu phone mới được cung cấp (khác null)
        // 2. Và phone mới khác phone hiện tại của người dùng
        // 3. Và phone mới đã tồn tại trong hệ thống (của người dùng khác)
        if (userUpdateProfileDTO.getPhone() != null &&
                !userUpdateProfileDTO.getPhone().equals(user.getPhone()) && // Đảm bảo phone mới khác phone cũ
                userRepository.existsByPhone(userUpdateProfileDTO.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        userMapper.updateUserFromDTO(user, userUpdateProfileDTO);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return userMapper.convertToUserProfileDTO(updatedUser);
    }
}
