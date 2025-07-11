package e_commerce.monolithic.service.admin;

import java.time.LocalDate;
import java.util.List;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.enums.CategoryType;

public interface CategoryService {

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt);

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId, Boolean isRoot);

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId, Boolean isRoot, CategoryType type);

    List<CategoryResponseDTO> findRootCategories();

    List<CategoryResponseDTO> findChildrenByParentId(Long parentId);

    CategoryResponseDTO findById(Long categoryId);

    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);

    CategoryResponseDTO updateCategory(Long categoryId ,CategoryUpdateDTO categoryUpdateDTO);

    ResponseMessageDTO deleteCategory(Long categoryId);

    // Additional hierarchy methods
    List<CategoryResponseDTO> getCategoryHierarchy();

    boolean canDeleteCategory(Long categoryId);

    List<CategoryResponseDTO> getCategoryPath(Long categoryId);
}
