package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.user.UserProfileDTO;
import e_commerce.monolithic.dto.user.UserUpdateProfileDTO;

import java.util.Optional;

public interface UserService {
    UserProfileDTO getUserprofile(String username);
    UserProfileDTO updateUserProfile(String username, UserUpdateProfileDTO userUpdateProfileDTO);
}
