package e_commerce.monolithic.mapper;


import e_commerce.monolithic.dto.auth.UserReponseDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserReponseDTO converToDTO (User user) {
        Set<String > roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
        return new UserReponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.isEnabled(),
                user.getCreatedAt(),
                roles
        );

    }

}
