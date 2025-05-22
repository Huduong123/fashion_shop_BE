package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.authorities.*;
import e_commerce.monolithic.service.AuthorityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorityControllerTest {

    @Mock
    private AuthorityService authorityService;

    @InjectMocks
    private AuthorityController authorityController;

    private AutoCloseable closeable;

    private AuthorityResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sampleResponse = AuthorityResponseDTO.builder()
                .id(1L)
                .username("admin")
                .authority("ROLE_ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllAuthorities() {
        when(authorityService.findAll()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<AuthorityResponseDTO>> response = authorityController.getAllAuthorities();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("ROLE_ADMIN", response.getBody().get(0).getAuthority());
        verify(authorityService, times(1)).findAll();
    }

    @Test
    void testCreateAuthority() {
        AuthorityCreateDTO createDTO = new AuthorityCreateDTO(1L, "ROLE_USER");
        when(authorityService.createAuthority(createDTO)).thenReturn(sampleResponse);

        ResponseEntity<AuthorityResponseDTO> response = authorityController.createAuthority(createDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("admin", response.getBody().getUsername());
        verify(authorityService, times(1)).createAuthority(createDTO);
    }

    @Test
    void testUpdateAuthority() {
        AuthorityUpdateDTO updateDTO = new AuthorityUpdateDTO(1L, "ROLE_MANAGER");
        when(authorityService.updateAuthority(updateDTO)).thenReturn(sampleResponse);

        ResponseEntity<AuthorityResponseDTO> response = authorityController.updateAuthority(updateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("admin", response.getBody().getUsername());
        verify(authorityService, times(1)).updateAuthority(updateDTO);
    }

    @Test
    void testDeleteAuthority() {
        Long idToDelete = 5L;

        doNothing().when(authorityService).deleteAuthority(idToDelete);

        ResponseEntity<String> response = authorityController.deleteAuthority(idToDelete);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Xóa authority thành công với ID : " + idToDelete, response.getBody());
        verify(authorityService, times(1)).deleteAuthority(idToDelete);
    }

    @Test
    void testSearchAuthorities() {
        String username = "admin";
        String authority = "ROLE_ADMIN";
        when(authorityService.search(username, authority)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<AuthorityResponseDTO>> response = authorityController.searchAuthorities(username, authority);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("admin", response.getBody().get(0).getUsername());
        verify(authorityService, times(1)).search(username, authority);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
