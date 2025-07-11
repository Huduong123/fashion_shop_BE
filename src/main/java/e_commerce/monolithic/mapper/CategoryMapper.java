package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponseDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setType(category.getType());
        dto.setStatus(category.getStatus());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        // Set parent information
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }

        // NOTE: Children information should be set separately in service layer to avoid
        // lazy loading
        // Default values
        dto.setChildrenCount(0);
        dto.setLevel(calculateLevel(category));

        return dto;
    }

    public CategoryResponseDTO convertToDTOWithoutChildren(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponseDTO dto = new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getType(),
                category.getStatus(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getParent() != null ? category.getParent().getName() : null);

        // Set default values to avoid lazy loading
        dto.setChildrenCount(0);
        dto.setLevel(calculateLevel(category));

        return dto;
    }

    public CategoryResponseDTO.CategorySummaryDTO convertToSummaryDTO(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponseDTO.CategorySummaryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                0 // Children count should be set separately to avoid lazy loading
        );
    }

    public Category createDtoToEntity(CategoryCreateDTO categoryCreateDTO) {
        if (categoryCreateDTO == null) {
            return null;
        }

        return Category.builder()
                .name(categoryCreateDTO.getName())
                .description(categoryCreateDTO.getDescription())
                .type(categoryCreateDTO.getType())
                .status(categoryCreateDTO.getStatus())
                // Parent will be set separately in service layer
                .build();
    }

    public void updateDtoToEntity(Category existingCategory, CategoryUpdateDTO categoryUpdateDTO) {
        if (existingCategory == null || categoryUpdateDTO == null) {
            return;
        }

        existingCategory.setName(categoryUpdateDTO.getName());
        existingCategory.setDescription(categoryUpdateDTO.getDescription());
        existingCategory.setType(categoryUpdateDTO.getType());
        existingCategory.setStatus(categoryUpdateDTO.getStatus());
        // Parent will be updated separately in service layer
    }

    private Integer calculateLevel(Category category) {
        if (category == null) {
            return null;
        }

        int level = 0;
        Category current = category.getParent();
        while (current != null) {
            level++;
            current = current.getParent();
        }
        return level;
    }
}
