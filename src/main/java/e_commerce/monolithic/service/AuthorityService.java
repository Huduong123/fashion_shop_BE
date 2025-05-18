package e_commerce.monolithic.service;

import e_commerce.monolithic.dto.admin.AuthorityDTO;
import e_commerce.monolithic.entity.Authority;

import java.util.List;

public interface AuthorityService {

    List<AuthorityDTO> findAll();

    AuthorityDTO createAuthority(Long userId, String authorityRole);

    AuthorityDTO updateAuthority(Long authorityId, String authorityRole);

    void deleteAuthority(Long authorityId);

    List<AuthorityDTO> search(String username, String authority);
}
