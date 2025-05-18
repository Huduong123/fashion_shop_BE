package e_commerce.monolithic.service;

import e_commerce.monolithic.dto.admin.AuthorityDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.mapper.AuthorityMapper;
import e_commerce.monolithic.repository.AuthorityRepository;
import e_commerce.monolithic.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImp implements AuthorityService{

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthorityServiceImp(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AuthorityDTO> findAll() {
        return authorityRepository.findAll()
                .stream()
                .map(AuthorityMapper::toAuthorityDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorityDTO createAuthority(Long userId, String authorityRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID" + userId + "not found"));

        Authority authority = new Authority();
        authority.setAuthority(authorityRole);
        authority.setUser(user);
        return AuthorityMapper.toAuthorityDTO(authorityRepository.save(authority));
    }

    @Override
    public AuthorityDTO updateAuthority(Long authorityId, String authorityRole) {
        Optional<Authority> optionalAuthority = authorityRepository.findById(authorityId);
        if(!optionalAuthority.isPresent()){
            throw new EntityNotFoundException("Authority with ID" + authorityId + "not found");
        }
        Authority authority = optionalAuthority.get();
        authority.setAuthority(authorityRole);

        return AuthorityMapper.toAuthorityDTO(authorityRepository.save(authority));
    }

    @Override
    public void deleteAuthority(Long authorityId) {
        if(!authorityRepository.existsById(authorityId)){
            throw new EntityNotFoundException("Authority with ID" + authorityId + "not found");
        }
        authorityRepository.deleteById(authorityId);
    }

    @Override
    public List<AuthorityDTO> search(String username, String authority) {
        return authorityRepository.searchByUsernameAndAuthority(username, authority)
                .stream()
                .map(AuthorityMapper::toAuthorityDTO)
                .collect(Collectors.toList());
    }
}
