package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.authorities.AuthorityDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.entity.Authority;

public class AuthorityMapper {
    public static AuthorityResponseDTO toResAuthorityDTO(Authority authority) {
        if (authority == null || authority.getUser() == null) {
            return null;
        }
        return AuthorityResponseDTO.builder()
                .id(authority.getId())
                .username(authority.getUser().getUsername())
                .authority(authority.getAuthority())
                .createdAt(authority.getCreatedAt())
                .updatedAt(authority.getUpdatedAt())
                .build();
    }
}
