package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.AuthorityDTO;
import e_commerce.monolithic.entity.Authority;

public class AuthorityMapper {
    public static AuthorityDTO toAuthorityDTO(Authority authority) {
        if (authority == null || authority.getUser() == null) {
            return null;
        }
        AuthorityDTO dto = new AuthorityDTO();
        dto.setId(authority.getId());
        dto.setAuthority(authority.getAuthority());
        dto.setUsername(authority.getUser().getUsername());
        return dto;
    }
}
