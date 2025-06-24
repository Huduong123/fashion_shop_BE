package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.dto.user.adress.UserAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserCreateAddressDTO;
import e_commerce.monolithic.dto.user.adress.UserUpdateAddressDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.entity.UserAddress;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.UserAddressMapper;
import e_commerce.monolithic.repository.UserAddressRepository;
import e_commerce.monolithic.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImp implements  UserAddressService{

    private final UserAddressMapper userAddressMapper;
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    public UserAddressServiceImp(UserAddressMapper userAddressMapper, UserAddressRepository userAddressRepository, UserRepository userRepository) {
        this.userAddressMapper = userAddressMapper;
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với username: " + username));
    }

    private UserAddress findUserAddressById(Long addressId) {
        return userAddressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy địa chỉ với ID: " + addressId));
    }

    @Override
    public List<UserAddressDTO> findAllAddressesForUser(String username) {
        User user = findUserByUsername(username);
        List<UserAddress> addresses = userAddressRepository.findByUserId(user.getId());
        return addresses.stream()
                .map(userAddressMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAddressDTO createAddress(UserCreateAddressDTO createAddressDTO, String username) {
        User user = findUserByUsername(username);

        if (Boolean.TRUE.equals(createAddressDTO.getIsDefault())) {
            userAddressRepository.unsetAllDefaultAddressForUser(user.getId());
        }

        UserAddress userAddress = userAddressMapper.createDtoToEntity(createAddressDTO, user);
        UserAddress savedAddress = userAddressRepository.save(userAddress);

        return userAddressMapper.convertToDTO(savedAddress);
    }

    @Override
    @Transactional
    public UserAddressDTO updateAddress(Long addressId, UserUpdateAddressDTO updateAddressDTO, String username) {
        UserAddress existingAddress = findUserAddressById(addressId);
        User user = findUserByUsername(username);

        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa địa chỉ này.");
        }

        if (Boolean.TRUE.equals(updateAddressDTO.getIsDefault())) {
            userAddressRepository.unsetAllDefaultAddressForUser(user.getId());
        }

        userAddressMapper.updateAddressDtoToEntity(updateAddressDTO, existingAddress);
        UserAddress updatedAddress = userAddressRepository.save(existingAddress);

        return userAddressMapper.convertToDTO(updatedAddress);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteAddress(Long addressId, String username) {
        UserAddress addressToDelete = findUserAddressById(addressId);
        User user = findUserByUsername(username);

        if (!addressToDelete.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa địa chỉ này.");
        }

        if (addressToDelete.isDefault()) {
            throw new IllegalArgumentException("Không thể xóa địa chỉ mặc định. Vui lòng đặt địa chỉ khác làm mặc định trước.");
        }

        return  new ResponseMessageDTO(HttpStatus.OK, "Địa chỉ đã được xóa thành công");
    }
}
