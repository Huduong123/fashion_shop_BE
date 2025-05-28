package e_commerce.monolithic.mapper;


import e_commerce.monolithic.dto.admin.AccountAdminDTO;
import e_commerce.monolithic.dto.admin.AccountCreateAdminDTO;
import e_commerce.monolithic.dto.admin.AccountUpdateAdminDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.dto.auth.UserReponseDTO;
import e_commerce.monolithic.dto.user.UserProfileDTO;
import e_commerce.monolithic.dto.user.UserUpdateProfileDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserReponseDTO converToDTO (User user) {
        Set<String > roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
        return new UserReponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate(),
                user.isEnabled(),
                user.getCreatedAt(),
                roles
        );
    }

    public User convertToEntity (UserRegisterDTO dto, Set<Authority> authorities, String endcodedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(endcodedPassword);
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setPhone(dto.getPhone());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setAuthorities(new ArrayList<>(authorities));
        return user;

    }


    public UserProfileDTO convertToUserProfileDTO (User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate()
        );
    }

    public void updateUserFromDTO (User user, UserUpdateProfileDTO userUpdateProfileDTO) {
        if (userUpdateProfileDTO.getEmail() != null) {
            user.setEmail(userUpdateProfileDTO.getEmail());
        }
        if (userUpdateProfileDTO.getFullname() != null) {
            user.setFullname(userUpdateProfileDTO.getFullname());
        }
        if (userUpdateProfileDTO.getPhone() != null) {
            user.setPhone(userUpdateProfileDTO.getPhone());
        }
        if (userUpdateProfileDTO.getGender() != null) {
            user.setGender(userUpdateProfileDTO.getGender());
        }
        if (userUpdateProfileDTO.getBirthday() != null) {
            user.setBirthDate(userUpdateProfileDTO.getBirthday());
        }

    }

    // ACcount
    public AccountAdminDTO convertToAdminDTO (User user) {
        return new AccountAdminDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public void updateAccountFromDTO (User user, AccountUpdateAdminDTO  accountUpdateAdminDTO) {
        if (accountUpdateAdminDTO.getEmail() != null) {
            user.setEmail(accountUpdateAdminDTO.getEmail());
        }
        if (accountUpdateAdminDTO.getFullname() != null) {
            user.setFullname(accountUpdateAdminDTO.getFullname());
        }
        if (accountUpdateAdminDTO.getPhone() != null) {
            user.setPhone(accountUpdateAdminDTO.getPhone());
        }
        if (accountUpdateAdminDTO.getGender() != null) {
            user.setGender(accountUpdateAdminDTO.getGender());
        }
        if (accountUpdateAdminDTO.getBirthday() != null) {
            user.setBirthDate(accountUpdateAdminDTO.getBirthday());
        }
        user.setEnabled(accountUpdateAdminDTO.isEnabled());
    }
}
