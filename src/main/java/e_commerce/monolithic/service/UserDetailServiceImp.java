package e_commerce.monolithic.service;

import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserDetailServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional  // Rất quan trọng để tránh lỗi LazyInitializationException
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Lấy danh sách authorities (quyền) từ user
        Set<GrantedAuthority> authorities = user.getAuthorities()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
