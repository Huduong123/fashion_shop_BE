package e_commerce.monolithic.dto.admin.category;

import e_commerce.monolithic.entity.enums.CategoryStatus;
import e_commerce.monolithic.entity.enums.CategoryType;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private CategoryType type;
    private CategoryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Parent category information
    private Long parentId;
    private String parentName;

    // Children categories (basic info only to avoid deep nesting)
    private List<CategorySummaryDTO> children;

    // Level in hierarchy (0 for root, 1 for first level child, etc.)
    private Integer level;

    // Total number of children
    private Integer childrenCount;

    // Constructor without children (for simple responses)
    public CategoryResponseDTO(Long id, String name, String description, CategoryType type, CategoryStatus status,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            Long parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentId = parentId;
        this.parentName = parentName;
    }

    // Inner class for basic category summary
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummaryDTO {
        private Long id;
        private String name;
        private String description;
        private Integer childrenCount;
    }
}
