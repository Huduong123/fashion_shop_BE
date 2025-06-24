package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.user.adress.UserAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserCreateAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserUpdateAddressDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.entity.UserAddress;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper {

    public UserAddressDTO convertToDTO(UserAddress userAddress) {

        if (userAddress == null) {
            return null;
        }
        return new UserAddressDTO(
                userAddress.getId(),
                userAddress.getUser().getId(),
                userAddress.getRecipientName(),
                userAddress.getPhoneNumber(),
                userAddress.getAddressDetail(),
                userAddress.isDefault(),
                userAddress.getCreatedAt(),
                userAddress.getUpdatedAt()
        );
    }

    public UserAddress createDtoToEntity(UserCreateAddressDTO createAddressDTO, User user) {
        if (createAddressDTO == null || user == null) {
            return null;
        }

        UserAddress userAddress = new UserAddress();
        userAddress.setUser(user);

        userAddress.setRecipientName(createAddressDTO.getRecipientName());
        userAddress.setPhoneNumber(createAddressDTO.getPhoneNumber());
        userAddress.setAddressDetail(createAddressDTO.getAddressDetail());
        userAddress.setDefault(createAddressDTO.getIsDefault() != null && createAddressDTO.getIsDefault());

        return userAddress;
    }

    public void updateAddressDtoToEntity (UserUpdateAddressDTO userUpdateAddressDTO, UserAddress existingUserAddress) {
        if (userUpdateAddressDTO == null || existingUserAddress == null) {
            return;
        }

        existingUserAddress.setRecipientName(userUpdateAddressDTO.getRecipientName());
        existingUserAddress.setPhoneNumber(userUpdateAddressDTO.getPhoneNumber());
        existingUserAddress.setAddressDetail(userUpdateAddressDTO.getAddressDetail());

        if (userUpdateAddressDTO.getIsDefault() != null) {
            existingUserAddress.setDefault(userUpdateAddressDTO.getIsDefault());
        }
    }
}
