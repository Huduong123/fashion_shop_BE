package e_commerce.monolithic.service.auth;

import e_commerce.monolithic.dto.auth.AdminLoginDTO;
import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserRegisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.service.common.UserValidationService;

import java.util.Optional;

public interface UserAuthService  {
    String login(UserLoginDTO userLoginDTO);
    String loginAdmin(AdminLoginDTO adminLoginDTO);
    User register(UserRegisterDTO userRegisterDTO);


}
