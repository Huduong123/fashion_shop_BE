package e_commerce.monolithic.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import e_commerce.monolithic.dto.admin.authorities.AuthorityCreateDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.admin.authorities.AuthorityDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.mapper.AuthorityMapper;
import e_commerce.monolithic.repository.AuthorityRepository;
import e_commerce.monolithic.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AuthorityServiceImp implements AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthorityServiceImp(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<AuthorityResponseDTO> findAll() {
        return authorityRepository.findAll()
                .stream()
                .map(AuthorityMapper::toResAuthorityDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorityResponseDTO createAuthority(AuthorityCreateDTO authorityCreateDTO) {
        if (!authorityCreateDTO.getAuthority().startsWith("ROLE_")) {
            throw new IllegalArgumentException("Authority roles must start with 'ROLE_'");
        }

        Long userId = authorityCreateDTO.getUserId();
        String authorityStr = authorityCreateDTO.getAuthority();

        if (authorityRepository.existsByUserIdAndAuthority(userId, authorityStr)) {
            throw new IllegalArgumentException("User with ID " + userId + " already have role " + authorityStr);
        }

        User user = userRepository.findById(authorityCreateDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID" + authorityCreateDTO.getUserId() + "not found"));

        Authority authority = new Authority();
        authority.setAuthority(authorityCreateDTO.getAuthority());
        authority.setUser(user);
        return AuthorityMapper.toResAuthorityDTO(authorityRepository.save(authority));
    }

    @Override
    @Transactional
    public AuthorityResponseDTO updateAuthority(AuthorityUpdateDTO authorityUpdateDTO) {
        Authority authority = authorityRepository.findById(authorityUpdateDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Authority with ID" + authorityUpdateDTO.getId() + "not found"));

        String newAuthority = authorityUpdateDTO.getAuthority();
        Long userId = authority.getUser().getId();

        boolean exists = authorityRepository.existsByUserIdAndAuthorityAndIdNot(userId, authority.getAuthority(), authority.getId());
        if (exists) {
            throw new IllegalArgumentException("User ID " + userId + " already have role " + newAuthority + " ");
        }

        authority.setAuthority(authorityUpdateDTO.getAuthority());
        return AuthorityMapper.toResAuthorityDTO(authorityRepository.save(authority));
    }

    @Override
    @Transactional
    public void deleteAuthority(Long authorityId) {
        if (!authorityRepository.existsById(authorityId)) {
            throw new EntityNotFoundException("Authority with ID" + authorityId + "not found");
        }
        authorityRepository.deleteById(authorityId);
    }

    @Override
    @Transactional
    public List<AuthorityResponseDTO> search(String username, String authority) {
        return authorityRepository.searchByUsernameAndAuthority(username, authority)
                .stream()
                .map(AuthorityMapper::toResAuthorityDTO)
                .collect(Collectors.toList());
    }


}
