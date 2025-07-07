package e_commerce.monolithic.service.auth;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.dto.auth.AdminLoginResponseDTO;
import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserLoginResponseDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;

public interface UserAuthService  {
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);
    AdminLoginResponseDTO loginAdmin(AdminLoginDTO adminLoginDTO);
    User register(UserRegisterDTO userRegisterDTO);


}
