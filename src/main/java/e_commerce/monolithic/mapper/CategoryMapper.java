package e_commerce.monolithic.mapper;


import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public Category createDtoToEntity(CategoryCreateDTO categoryCreateDTO) {
        if (categoryCreateDTO == null) {
            return null;
        }

        return Category.builder()
                .name(categoryCreateDTO.getName())
                .description(categoryCreateDTO.getDescription())
                .build();
    }

    public void updateDtoToEntity(Category existingCategory, CategoryUpdateDTO categoryUpdateDTO) {
        if (existingCategory == null || categoryUpdateDTO == null) {
            return;
        }

        existingCategory.setName(categoryUpdateDTO.getName());
        existingCategory.setDescription(categoryUpdateDTO.getDescription());
    }

}
