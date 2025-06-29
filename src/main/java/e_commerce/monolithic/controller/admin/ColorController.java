package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.color.ColorCreateDTO;
import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.dto.admin.color.ColorUpdateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.service.admin.ColorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<ColorResponseDTO>> findAllColors() {
        List<ColorResponseDTO> colors = colorService.findAllColors();
        return ResponseEntity.ok(colors);
    }

    @GetMapping("/{colorId}")
    public ResponseEntity<ColorResponseDTO> findColorById(@PathVariable("colorId") long colorId) {
        ColorResponseDTO color = colorService.findColorById(colorId);
        return ResponseEntity.ok(color);
    }

    @PostMapping()
    public ResponseEntity<ColorResponseDTO> createColor(@Valid @RequestBody ColorCreateDTO colorCreateDTO) {
        ColorResponseDTO color = colorService.createColor(colorCreateDTO);
        return new ResponseEntity<>(color, HttpStatus.CREATED);
    }

    @PutMapping("/{colorId}")
    public ResponseEntity<ColorResponseDTO> updateColor(@PathVariable Long colorId,
                                                        @Valid @RequestBody ColorUpdateDTO colorUpdateDTO) {
        ColorResponseDTO colorResponseDTO = colorService.updateColorById(colorUpdateDTO,colorId);
        return  ResponseEntity.ok(colorResponseDTO);
    }

    @DeleteMapping("/{colorId}")
    public ResponseEntity<ResponseMessageDTO> deleteColor(@PathVariable long colorId) {
        ResponseMessageDTO responseMessageDTO = colorService.deleteColorById(colorId);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
