package e_commerce.monolithic.controller.user;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.service.user.UserColorService;

@RestController
@RequestMapping("/api/users/colors")

public class UserColorController {
    private final UserColorService userColorService;

    public UserColorController(UserColorService userColorService) {
        this.userColorService = userColorService;
    }
    
       @GetMapping
    public ResponseEntity<List<ColorResponseDTO>> getAllColors() {
        List<ColorResponseDTO> colors = userColorService.getAllColors();
        return ResponseEntity.ok(colors);
    }
}
