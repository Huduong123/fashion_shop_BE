package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.UserAdminDTO;
import e_commerce.monolithic.dto.admin.UserCreateAdminDTO;
import e_commerce.monolithic.dto.admin.UserUpdateAdminDTO;

import java.util.List;

public interface AccountService {
    List<UserAdminDTO> getAllAccount();

    UserAdminDTO createAccount(UserCreateAdminDTO userCreateAdminDTO);

    UserAdminDTO updateAccount(UserUpdateAdminDTO userUpdateAdminDTO);

    void deleteAccount(Long userId);

    UserAdminDTO getAccountById(Long id);

}
