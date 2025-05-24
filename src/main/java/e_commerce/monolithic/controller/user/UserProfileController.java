package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.UserChangePasswordDTO;
import e_commerce.monolithic.dto.user.UserProfileDTO;
import e_commerce.monolithic.dto.user.UserUpdateProfileDTO;
import e_commerce.monolithic.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getCurentUserProfile(Principal principal) {
        String username = principal.getName();
        UserProfileDTO userProfile = userService.getUserprofile(username);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateCurrentUserProfile(
            @RequestBody @Valid UserUpdateProfileDTO updateProfileDTO
            , Principal principal) {
        String username = principal.getName();
        UserProfileDTO updatedUserProfile= userService.updateUserProfile(username, updateProfileDTO);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessageDTO> changePassword(
            @RequestBody @Valid UserChangePasswordDTO  userChangePasswordDTO,
            Principal principal
            ) {
        String username = principal.getName();
        ResponseMessageDTO responseMessageDTO = userService.changePassword(username, userChangePasswordDTO);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
