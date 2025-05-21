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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthorityServiceImp implements AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthorityServiceImp.class);

    @Autowired
    public AuthorityServiceImp(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<AuthorityResponseDTO> findAll() {

        List<Authority> authorities = authorityRepository.findAll();
        logger.info("Danh sách authority");
        for (Authority auth : authorities) {
            logger.info("Authority ID: {}, Role: {}, User: {}",
                    auth.getId(),
                    auth.getAuthority(),
                    auth.getUser() != null ? auth.getUser().getUsername() : "N/A");
        }
        return authorities.stream()
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

        Authority savedAuthority = authorityRepository.save(authority);

        logger.info("Authority '{}' đã được tạo thành công cho User '{}'", savedAuthority.getAuthority(), user.getUsername());

        return AuthorityMapper.toResAuthorityDTO(savedAuthority);
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

        authority.setAuthority(newAuthority);
        Authority updatedAuthority = authorityRepository.save(authority);

        logger.info("Authority ID {} đã được cập nhật thành '{}' cho User '{}'", updatedAuthority.getId(), updatedAuthority.getAuthority(), updatedAuthority.getUser().getUsername());

        return AuthorityMapper.toResAuthorityDTO(updatedAuthority);
    }

    @Override
    @Transactional
    public void deleteAuthority(Long authorityId) {
        if (!authorityRepository.existsById(authorityId)) {
            logger.warn("Delete fail: No find authority with ID {}", authorityId);
            throw new EntityNotFoundException("Authority with ID" + authorityId + "not found");
        }
        authorityRepository.deleteById(authorityId);
        logger.info("Delete Authority with ID {}", authorityId);
    }

    @Override
    @Transactional
    public List<AuthorityResponseDTO> search(String username, String authority) {
        List<Authority> result = authorityRepository.searchByUsernameAndAuthority(username, authority);

        logger.info("Đã tìm thấy {} authority khới với username='{}' và authority='{}'",
                result.size(), username, authority);
        for (Authority auth : result) {
            logger.info("Find: Authority with ID {}, Role: {}, User: {}",
                    auth.getId(), auth.getAuthority(), auth.getUser().getUsername());
        }
        return result.stream()
                .map(AuthorityMapper::toResAuthorityDTO)
                .collect(Collectors.toList());
    }


}
