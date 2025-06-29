package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

import java.time.LocalDate;
import java.util.List;

public interface CategoryService {

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt);

    CategoryResponseDTO findById(Long categoryId);

    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);

    CategoryResponseDTO updateCategory(Long categoryId ,CategoryUpdateDTO categoryUpdateDTO);

    ResponseMessageDTO deleteCategory(Long categoryId);
}
