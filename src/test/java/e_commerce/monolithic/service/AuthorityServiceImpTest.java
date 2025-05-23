package e_commerce.monolithic.service;


import e_commerce.monolithic.dto.admin.authorities.AuthorityCreateDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityUpdateDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;
import e_commerce.monolithic.repository.AuthorityRepository;
import e_commerce.monolithic.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class AuthorityServiceImpTest {

    @InjectMocks
    private AuthorityServiceImp authorityServiceImp;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //===================Success Test Cases==============
    @Test
    void testFindAll_Success() {
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setAuthority("ROLE_ADMIN");
        User user = new User();
        user.setUsername("David Laid");
        authority.setUser(user);

        when(authorityRepository.findAll()).thenReturn(List.of(authority));

        List<AuthorityResponseDTO> result = authorityServiceImp.findAll();

        assertEquals(1, result.size());
        assertEquals("ROLE_ADMIN", result.get(0).getAuthority());
        assertEquals("David Laid", result.get(0).getUsername());
    }
    @Test
    void testCreateAuthority_Success() {
        AuthorityCreateDTO AuCreateDTO = new AuthorityCreateDTO(1L, "ROLE_USER");
        User user = new User();
        user.setId(1L);
        user.setUsername("David");

        when(authorityRepository.existsByUserIdAndAuthority(1L, "ROLE_USER")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Authority saveAuthority = new Authority();
        saveAuthority.setId(1L);
        saveAuthority.setAuthority("ROLE_USER");
        saveAuthority.setUser(user);

        when(authorityRepository.save(any(Authority.class))).thenReturn(saveAuthority);

        AuthorityResponseDTO result = authorityServiceImp.createAuthority(AuCreateDTO);

        assertEquals("ROLE_USER", result.getAuthority());
        assertEquals("David", result.getUsername());
    }
    @Test
    void testUpdateAuthority_Success() {
        AuthorityUpdateDTO AuUpdateDTO = new AuthorityUpdateDTO(1L, "ROLE_MANAGER");

        Authority existingAuthority = new Authority();
        existingAuthority.setId(5L);
        existingAuthority.setAuthority("ROLE_USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("David");
        existingAuthority.setUser(user);

        when(authorityRepository.findById(1L)).thenReturn(Optional.of(existingAuthority));
        when(authorityRepository.existsByUserIdAndAuthorityAndIdNot(1L, "ROLE_MANAGER", 5L)).thenReturn(false);
        when(authorityRepository.save(existingAuthority)).thenReturn(existingAuthority);

        AuthorityResponseDTO result = authorityServiceImp.updateAuthority(AuUpdateDTO);

        assertEquals("ROLE_MANAGER", existingAuthority.getAuthority());
        assertEquals("David", result.getUsername());
    }

    @Test
    void testDeleteAuthority_Success() {
        when(authorityRepository.existsById(1L)).thenReturn(true);

        authorityServiceImp.deleteAuthority(1L);

        verify(authorityRepository, times(1)).existsById(1L);
    }
    @Test
    void testSearchAuthorityAndUserName_Success() {
        Authority a1 = new Authority();
        a1.setId(1L);
        a1.setAuthority("ROLE_ADMIN");
        User user = new User();
        user.setId(1L);
        user.setUsername("admin1");
        a1.setUser(user);

        Authority a2 = new Authority();
        a2.setId(2L);
        a2.setAuthority("ROLE_ADMIN");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("admin2");
        a2.setUser(user2);
        when(authorityRepository.searchByUsernameAndAuthority("admin", "ROLE_ADMIN"))
                .thenReturn(List.of(a1,a2));

        List<AuthorityResponseDTO> result = authorityServiceImp.search("admin", "ROLE_ADMIN");

        assertEquals(2, result.size());

        assertEquals("ROLE_ADMIN", result.get(0).getAuthority());
        assertEquals("admin1", result.get(0).getUsername());

        assertEquals("ROLE_ADMIN", result.get(1).getAuthority());
        assertEquals("admin2", result.get(1).getUsername());
    }


    // =================== TEST FAIL CASES -===================
    @Test
    void testCreateAuthority_Fail_InvalidRoleFormat(){
        AuthorityCreateDTO dto  = new AuthorityCreateDTO(1L, "USER");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            authorityServiceImp.createAuthority(dto);
        });

        assertEquals("Authority roles must start with 'ROLE_'", ex.getMessage());
    }

    @Test
    void testCreateAuthority_Fail_UserAlreadyHasRole(){
        AuthorityCreateDTO dto  = new AuthorityCreateDTO(1L, "ROLE_ADMIN");

        when(authorityRepository.existsByUserIdAndAuthority(1L, "ROLE_ADMIN")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            authorityServiceImp.createAuthority(dto);
        });

        assertEquals("User with ID 1 already have role ROLE_ADMIN", ex.getMessage());
    }

    @Test
    void testCreateAuthority_Fail_UserNotFound(){
        AuthorityCreateDTO dto  = new AuthorityCreateDTO(999L, "ROLE_ADMIN");

        when(authorityRepository.existsByUserIdAndAuthority(999L, "ROLE_ADMIN")).thenReturn(false);
        when(authorityRepository.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> {
            authorityServiceImp.createAuthority(dto);
        });

        assertEquals("User with ID 999 not found", ex.getMessage());
    }

    @Test
    void testUpdateAuthority_Fail_UserNotFound(){
        AuthorityUpdateDTO dto  = new AuthorityUpdateDTO(100L, "ROLE_ADMIN");

        when(authorityRepository.findById(100L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class, () -> {
            authorityServiceImp.updateAuthority(dto);
        });
        assertEquals("Authority with ID 100 not found", ex.getMessage());
    }
    @Test
    void testUpdateAuthority_Fail_UserAlreadyHasRoleWithDifferentId() {
        AuthorityUpdateDTO dto = new AuthorityUpdateDTO(1L, "ROLE_ADMIN");

        Authority existingAuthority = new Authority();
        existingAuthority.setId(1L);
        existingAuthority.setAuthority("ROLE_USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("John");
        existingAuthority.setUser(user);

        when(authorityRepository.findById(1L)).thenReturn(Optional.of(existingAuthority));
        when(authorityRepository.existsByUserIdAndAuthorityAndIdNot(user.getId(), dto.getAuthority(), existingAuthority.getId())).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            authorityServiceImp.updateAuthority(dto);
        });

        assertEquals("User ID 1 already have role ROLE_ADMIN ", ex.getMessage());
    }
@Test
        void testDeleteAuthoriy_Fail_AuthorityNotFound(){
            when(authorityRepository.existsById(99L)).thenReturn(false);

            Exception ex = assertThrows(EntityNotFoundException.class, () -> {
                authorityServiceImp.deleteAuthority(99L);
            });

            assertEquals("Authority with ID 99 not found", ex.getMessage());
        }
}
