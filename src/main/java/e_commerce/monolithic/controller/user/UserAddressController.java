package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.adress.UserAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserCreateAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserUpdateAddressDTO;
import e_commerce.monolithic.service.user.UserAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users/address")
public class UserAddressController {

    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    /**
     * Lấy tất cả địa chỉ của người dùng đang đăng nhập.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserAddressDTO>> getCurrentUserAddresses(Principal principal) {
        String username = principal.getName();
        List<UserAddressDTO> addresses = userAddressService.findAllAddressesForUser(username);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Tạo một địa chỉ mới cho người dùng đang đăng nhập.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressDTO> createAddress(@Valid @RequestBody UserCreateAddressDTO createAddressDTO, Principal principal) {
        String username = principal.getName();
        UserAddressDTO createdAddress = userAddressService.createAddress(createAddressDTO, username);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    /**
     * Cập nhật địa chỉ cho người dùng đang đăng nhập.
     * Hệ thống sẽ tự kiểm tra xem địa chỉ này có thuộc về họ không.
     */
    @PutMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressDTO> updateAddress(@PathVariable Long addressId,
                                                        @Valid @RequestBody UserUpdateAddressDTO updateAddressDTO,
                                                        Principal principal) {
        String username = principal.getName();
        UserAddressDTO updatedAddress = userAddressService.updateAddress(addressId, updateAddressDTO, username);
        return ResponseEntity.ok(updatedAddress);
    }

    /**
     * Xóa một địa chỉ của người dùng đang đăng nhập.
     */
    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessageDTO> deleteAddress(@PathVariable Long addressId, Principal principal) {
        String username = principal.getName();
        ResponseMessageDTO response = userAddressService.deleteAddress(addressId, username);
        return ResponseEntity.ok(response);
    }
}
