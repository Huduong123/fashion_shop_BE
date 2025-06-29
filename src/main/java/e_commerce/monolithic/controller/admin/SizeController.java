package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.dto.admin.size.SizeCreateDTO;
import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.dto.admin.size.SizeUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.service.admin.SizeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sizes")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping
    public ResponseEntity<List<SizeResponseDTO>> getAllSizes() {
        List<SizeResponseDTO> sizes = sizeService.getAllSize();
        return  ResponseEntity.ok(sizes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SizeResponseDTO> getSizeById(@PathVariable("id") long id) {
        SizeResponseDTO size = sizeService.getSizeById(id);
        return  ResponseEntity.ok(size);
    }

    @PostMapping()
    public ResponseEntity<SizeResponseDTO> createSize(@Valid @RequestBody SizeCreateDTO sizeCreateDTO) {
        SizeResponseDTO size = sizeService.create(sizeCreateDTO);
        return  new ResponseEntity<>(size, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SizeResponseDTO> updateSize(@PathVariable("id") long id, @Valid @RequestBody SizeUpdateDTO sizeUpdateDTO) {
        SizeResponseDTO sizeResponseDTO = sizeService.update(sizeUpdateDTO, id);
        return  ResponseEntity.ok(sizeResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> deleteSize(@PathVariable("id") long id) {
        ResponseMessageDTO responseMessageDTO = sizeService.delete(id);
        return  ResponseEntity.ok(responseMessageDTO);
    }
}
