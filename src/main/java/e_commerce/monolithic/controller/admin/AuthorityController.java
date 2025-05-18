package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.AuthorityDTO;
import e_commerce.monolithic.service.AuthorityService;
import jakarta.validation.constraints.NotBlank;
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
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.findAll());
    }

    @PostMapping
    public ResponseEntity<AuthorityDTO> createAuthority(
        @RequestParam Long userId,
        @RequestParam @NotBlank String authority
    ){
        AuthorityDTO createdAuthority = authorityService.createAuthority(userId, authority);
        return ResponseEntity.ok(createdAuthority);
    }

    @PutMapping
    public ResponseEntity<AuthorityDTO> updateAuthority(
            @PathVariable("id") Long authorityId,
            @RequestParam @NotBlank String authority
    ){
        AuthorityDTO updatedAuthority = authorityService.updateAuthority(authorityId, authority);
        return ResponseEntity.ok(updatedAuthority);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable("id") Long authorityId) {
        authorityService.deleteAuthority(authorityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthorityDTO>> searchAuthorities(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String authority
    ) {
        List<AuthorityDTO> result = authorityService.search(username, authority);
        return ResponseEntity.ok(result);
    }
}
