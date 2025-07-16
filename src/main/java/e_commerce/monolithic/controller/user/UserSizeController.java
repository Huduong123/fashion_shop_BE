package e_commerce.monolithic.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.service.user.UserSizeService;

@RestController
@RequestMapping("/api/users/sizes") 
public class UserSizeController {
    private final UserSizeService userSizeService;

    public UserSizeController(UserSizeService userSizeService) {
        this.userSizeService = userSizeService;
    }

    @GetMapping
    public ResponseEntity<List<SizeResponseDTO>> getAllSizes() {
        List<SizeResponseDTO> sizes = userSizeService.getAllSizes();
        return ResponseEntity.ok(sizes);
    }
}
