package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.authorities.AuthorityCreateDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityUpdateDTO;

import java.util.List;

public interface AuthorityService {

    List<AuthorityResponseDTO> findAll();

    AuthorityResponseDTO createAuthority(AuthorityCreateDTO authorityCreateDTO);

    AuthorityResponseDTO updateAuthority(AuthorityUpdateDTO authorityUpdateDTO);

    void deleteAuthority(Long authorityId);

    List<AuthorityResponseDTO> search(String username, String authority);


}
