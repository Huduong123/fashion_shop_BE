package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.service.admin.CategoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/categories")
public class UserCategoryController {

    private final CategoryService categoryService;

    public UserCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAt,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Boolean isRoot) {

        List<CategoryResponseDTO> categories = categoryService.findAll(name, createdAt, updatedAt, parentId, isRoot);
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

    @GetMapping("/{id}/path")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryPath(@PathVariable Long id) {
        List<CategoryResponseDTO> path = categoryService.getCategoryPath(id);
        return ResponseEntity.ok(path);
    }
}
