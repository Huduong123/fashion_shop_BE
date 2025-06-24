package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.adress.UserAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserCreateAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserUpdateAddressDTO;

import java.util.List;

public interface UserAddressService {
    List<UserAddressDTO> findAllAddressesForUser(String username);

    UserAddressDTO createAddress(UserCreateAddressDTO createAddressDTO, String username);

    UserAddressDTO updateAddress(Long addressId, UserUpdateAddressDTO updateAddressDTO, String username);

    ResponseMessageDTO deleteAddress(Long addressId, String username);

}
