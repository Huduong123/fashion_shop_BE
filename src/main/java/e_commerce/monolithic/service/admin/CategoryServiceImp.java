package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.CategoryMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO findById(Long categoryId) {
        Category category = findCategoryById(categoryId);

        return categoryMapper.convertToDTO(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {

        // gọi hàm check lỗi
        checkCategoryNameExistOnCreate(categoryCreateDTO.getName());

        // chuyển đổi DTO sang Entity
        Category newCategory = categoryMapper.createDtoToEntity(categoryCreateDTO);

        // lưu vào DB
        Category savedCategory = categoryRepository.save(newCategory);

        // chuyển đổi entity đã lưu sang DTO trả về
        return categoryMapper.convertToDTO(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryUpdateDTO categoryUpdateDTO) {

        Category existingCategory = findCategoryById(categoryId);

        // check lỗi trùng tên khi cập nhật
        checkCategoryNameExistOnUpdate(categoryUpdateDTO.getName(), categoryId);

        // chuyển DTO sang entity
        categoryMapper.updateDtoToEntity(existingCategory,categoryUpdateDTO);

        // lưu
        Category savedCategory = categoryRepository.save(existingCategory);

        // chuyển entity sang DTO
        return categoryMapper.convertToDTO(savedCategory);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteCategory(Long categoryId) {

        Category categoryDelete = findCategoryById(categoryId);

        // kiểm tra danh mục có được sử dụng
        checkIfCategoryIsInUse(categoryDelete);

        categoryRepository.delete(categoryDelete);

        return new ResponseMessageDTO(HttpStatus.OK, "Danh mục đã đươc xóa thành công");
    }

    // Hàm helper check lỗi
    /**
     * Hàm private để tìm một danh mục theo ID.
     * Ném ra NotFoundException nếu không tìm thấy.
     * @param categoryId ID của danh mục cần tìm.
     * @return Entity Category nếu tìm thấy.
     * @throws NotFoundException nếu không có danh mục nào với ID đã cho.
     */
    private Category findCategoryById(Long categoryId) {
        return  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục với ID: " + categoryId));
    }
    /**
     * Hàm private để kiểm tra tên danh mục đã tồn tại khi tạo mới.
     * @param name Tên danh mục cần kiểm tra.
     * @throws IllegalArgumentException nếu tên đã tồn tại.
     */
    private void checkCategoryNameExistOnCreate(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tên danh mục '" + name + "' đã tồn tại.");
        }
    }
    /**
     * Hàm private để kiểm tra tên danh mục đã tồn tại khi cập nhật.
     * @param name Tên danh mục mới.
     * @param categoryId ID của danh mục đang được cập nhật.
     * @throws IllegalArgumentException nếu tên đã được sử dụng bởi một danh mục khác.
     */
    private void checkCategoryNameExistOnUpdate(String name, Long categoryId) {
        if (categoryRepository.existsByNameAndIdNot(name, categoryId)) {
            throw new IllegalArgumentException("Tên danh mục '" + name + "' đã được sử dụng bởi 1 danh mục khác.");
        }
    }

    /**
     * Hàm private để kiểm tra xem danh mục có đang được sử dụng bởi sản phẩm nào không.
     * @param category Đối tượng Category cần kiểm tra.
     * @throws IllegalArgumentException nếu danh mục có chứa sản phẩm.
     */
    private void checkIfCategoryIsInUse (Category category) {
        if (!category.getProducts().isEmpty()) {
            throw  new IllegalArgumentException("Không thể xóa danh mục '" + category.getName() + "' vì nó đang chứa sản phẩm");
        }
    }


}
