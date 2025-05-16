package e_commerce.monolithic.service;

import e_commerce.monolithic.dto.auth.UserLoginDTO;
import e_commerce.monolithic.dto.auth.UserResgisterDTO;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {
        Optional<User> userOpt = userRepository.findByUsername(userLoginDTO.getUserName());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            return user.getPassWord().equals(userLoginDTO.getPassWord()) && user.isEnabled();
        }
        return false;
    }


    @Override
    public User register(UserResgisterDTO userResgisterDTO) {
        User user = new User();
        user.setUserName(userResgisterDTO.getUserName());
        user.setPassWord(userResgisterDTO.getPassWord());
        user.setEmail(userResgisterDTO.getEmail());
        user.setFullName(userResgisterDTO.getFullName());
        user.setPhone(userResgisterDTO.getPhone());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
