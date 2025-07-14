package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.enums.CategoryType;
import e_commerce.monolithic.service.admin.CategoryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Read operations - có thể xem được bởi ADMIN/SYSTEM
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAt,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Boolean isRoot,
            @RequestParam(required = false) CategoryType type) {

        List<CategoryResponseDTO> categories = categoryService.findAll(name, createdAt, updatedAt, parentId, isRoot,
                type);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponseDTO>> getRootCategories() {
        List<CategoryResponseDTO> rootCategories = categoryService.findRootCategories();
        return ResponseEntity.ok(rootCategories);
    }

    @GetMapping("/hierarchy")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryHierarchy() {
        List<CategoryResponseDTO> hierarchy = categoryService.getCategoryHierarchy();
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryResponseDTO>> getChildrenByParentId(@PathVariable Long parentId) {
        List<CategoryResponseDTO> children = categoryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findBySlug(slug);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryPath(@PathVariable Long id) {
        List<CategoryResponseDTO> path = categoryService.getCategoryPath(id);
        return ResponseEntity.ok(path);
    }

    @GetMapping("/{id}/can-delete")
    public ResponseEntity<Boolean> canDeleteCategory(@PathVariable Long id) {
        boolean canDelete = categoryService.canDeleteCategory(id);
        return ResponseEntity.ok(canDelete);
    }

    // Write operations - chỉ ADMIN và SYSTEM được phép
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryCreateDTO);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.updateCategory(id, updateDTO);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<ResponseMessageDTO> deleteCategory(@PathVariable Long id) {
        ResponseMessageDTO responseMessageDTO = categoryService.deleteCategory(id);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
