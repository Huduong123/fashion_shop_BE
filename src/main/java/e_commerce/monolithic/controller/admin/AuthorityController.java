package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.authorities.AuthorityCreateDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityUpdateDTO;
import e_commerce.monolithic.service.admin.AuthorityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorityResponseDTO>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.findAll());
    }

    @PostMapping
    public ResponseEntity<AuthorityResponseDTO> createAuthority(@RequestBody @Valid AuthorityCreateDTO authorityCreateDTO) {
        AuthorityResponseDTO created = authorityService.createAuthority(authorityCreateDTO);
        return   ResponseEntity.ok(created);
    }

    @PutMapping
    public ResponseEntity<AuthorityResponseDTO> updateAuthority(
            @RequestBody @Valid AuthorityUpdateDTO authorityUpdateDTO
    ) {
        AuthorityResponseDTO updated = authorityService.updateAuthority(authorityUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthority(@PathVariable("id") Long authorityId) {
        authorityService.deleteAuthority(authorityId);
        return ResponseEntity.ok("Xóa authority thành công với ID : "+ authorityId );
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthorityResponseDTO>> searchAuthorities(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String authority
    ) {
        List<AuthorityResponseDTO> result = authorityService.search(username, authority);
        return ResponseEntity.ok(result);
    }
}
