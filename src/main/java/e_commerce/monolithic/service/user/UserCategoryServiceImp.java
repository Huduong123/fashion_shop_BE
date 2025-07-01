package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.mapper.CategoryMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCategoryServiceImp implements UserCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public UserCategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
