package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;

import java.util.List;

public interface UserCategoryService {

    List<CategoryResponseDTO> findAll();
}
