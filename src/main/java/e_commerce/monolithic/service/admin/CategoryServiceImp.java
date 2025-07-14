package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.category.CategoryCreateDTO;
import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.category.CategoryUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.enums.CategoryType;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.CategoryMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import e_commerce.monolithic.specification.CategorySpecification;
import e_commerce.monolithic.utils.SlugUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategorySpecification categorySpecification;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
            CategorySpecification categorySpecification) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categorySpecification = categorySpecification;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt) {
        return findAll(name, createdAt, updatedAt, null, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId,
            Boolean isRoot) {
        return findAll(name, createdAt, updatedAt, parentId, isRoot, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId,
            Boolean isRoot, CategoryType type) {
        Specification<Category> spec = categorySpecification.findByCriteria(name, createdAt, updatedAt, parentId,
                isRoot, type);

        return categoryRepository.findAll(spec)
                .stream()
                .map(categoryMapper::convertToDTOWithoutChildren) // Avoid deep nesting in list view
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findRootCategories() {
        // Use existing method and convert with children count from DB
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(category -> {
                    CategoryResponseDTO dto = categoryMapper.convertToDTOWithoutChildren(category);
                    // Count actual children categories
                    List<Category> children = categoryRepository.findByParentId(category.getId());
                    dto.setChildrenCount(children.size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findChildrenByParentId(Long parentId) {
        List<Category> children = categoryRepository.findByParentId(parentId);

        return children.stream()
                .map(category -> {
                    CategoryResponseDTO dto = categoryMapper.convertToDTOWithoutChildren(category);
                    // Count children for each category
                    List<Category> grandChildren = categoryRepository.findByParentId(category.getId());
                    dto.setChildrenCount(grandChildren.size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long categoryId) {
        Category category = findCategoryById(categoryId);

        // Build DTO manually to avoid lazy loading issues
        CategoryResponseDTO dto = categoryMapper.convertToDTOWithoutChildren(category);

        // Count actual children categories
        List<Category> children = categoryRepository.findByParentId(categoryId);
        dto.setChildrenCount(children.size());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục với slug: " + slug));

        // Build DTO manually to avoid lazy loading issues
        CategoryResponseDTO dto = categoryMapper.convertToDTOWithoutChildren(category);

        // Count actual children categories
        List<Category> children = categoryRepository.findByParentId(category.getId());
        dto.setChildrenCount(children.size());

        return dto;
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        // Validate parent if specified
        Category parentCategory = null;
        if (categoryCreateDTO.getParentId() != null) {
            parentCategory = findCategoryById(categoryCreateDTO.getParentId());

            // Validate parent category type - LINK categories cannot have children
            if (!parentCategory.canContainChildren()) {
                throw new IllegalArgumentException(
                        "Không thể tạo danh mục con cho danh mục '" + parentCategory.getName() +
                                "' vì danh mục này có kiểu '" + parentCategory.getType().getDisplayName() + "'. " +
                                "Chỉ có thể tạo danh mục con cho danh mục có kiểu 'Thư mục'.");
            }
        }

        // Check if category name exists within the same parent
        checkCategoryNameExistOnCreate(categoryCreateDTO.getName(), categoryCreateDTO.getParentId());

        // Convert DTO to Entity
        Category newCategory = categoryMapper.createDtoToEntity(categoryCreateDTO);

        // Generate slug if not provided
        if (newCategory.getSlug() == null || newCategory.getSlug().trim().isEmpty()) {
            String baseSlug = SlugUtils.generateSlug(newCategory.getName());
            String uniqueSlug = generateUniqueSlug(baseSlug);
            newCategory.setSlug(uniqueSlug);
        } else {
            // Validate provided slug
            checkSlugExistOnCreate(newCategory.getSlug());
        }

        // Set parent if specified
        if (parentCategory != null) {
            newCategory.setParent(parentCategory);
        }

        // Save to DB
        Category savedCategory = categoryRepository.save(newCategory);

        // Return converted DTO using findById to get fresh data
        return findById(savedCategory.getId());
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long categoryId, CategoryUpdateDTO categoryUpdateDTO) {
        Category existingCategory = findCategoryById(categoryId);

        // Validate parent if specified
        Category parentCategory = null;
        if (categoryUpdateDTO.getParentId() != null) {
            parentCategory = findCategoryById(categoryUpdateDTO.getParentId());

            // Validate parent category type - LINK categories cannot have children
            if (!parentCategory.canContainChildren()) {
                throw new IllegalArgumentException("Không thể di chuyển danh mục vào '" + parentCategory.getName() +
                        "' vì danh mục này có kiểu '" + parentCategory.getType().getDisplayName() + "'. " +
                        "Chỉ có thể di chuyển danh mục vào danh mục có kiểu 'Thư mục'.");
            }

            // Check for circular reference
            checkCircularReference(categoryId, categoryUpdateDTO.getParentId());
        }

        // Check name uniqueness within the same parent
        checkCategoryNameExistOnUpdate(categoryUpdateDTO.getName(), categoryId, categoryUpdateDTO.getParentId());

        // Validate type change - cannot change to LINK if category has children
        if (categoryUpdateDTO.getType() == CategoryType.LINK && categoryRepository.hasChildren(categoryId)) {
            throw new IllegalArgumentException("Không thể thay đổi danh mục '" + existingCategory.getName() +
                    "' sang kiểu 'Liên kết' vì danh mục này đang có danh mục con. " +
                    "Vui lòng xóa tất cả danh mục con trước khi thay đổi kiểu.");
        }

        // Update basic fields
        categoryMapper.updateDtoToEntity(existingCategory, categoryUpdateDTO);

        // Handle slug update
        if (categoryUpdateDTO.getSlug() == null || categoryUpdateDTO.getSlug().trim().isEmpty()) {
            // If slug not provided, regenerate from name if name changed OR if existing
            // category doesn't have slug
            if (!existingCategory.getName().equals(categoryUpdateDTO.getName()) || existingCategory.getSlug() == null) {
                String baseSlug = SlugUtils.generateSlug(categoryUpdateDTO.getName());
                String uniqueSlug = generateUniqueSlugForUpdate(baseSlug, categoryId);
                existingCategory.setSlug(uniqueSlug);
            }
        } else {
            // Validate provided slug
            checkSlugExistOnUpdate(categoryUpdateDTO.getSlug(), categoryId);
        }

        // Update parent
        existingCategory.setParent(parentCategory);

        // Save
        Category savedCategory = categoryRepository.save(existingCategory);

        // Return converted DTO using findById to get fresh data
        return findById(savedCategory.getId());
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteCategory(Long categoryId) {
        Category categoryToDelete = findCategoryById(categoryId);

        // Check if category can be deleted
        if (!canDeleteCategory(categoryId)) {
            throw new IllegalArgumentException("Không thể xóa danh mục '" + categoryToDelete.getName() +
                    "' vì nó đang chứa sản phẩm hoặc có danh mục con");
        }

        categoryRepository.delete(categoryToDelete);

        return new ResponseMessageDTO(HttpStatus.OK, "Danh mục đã được xóa thành công");
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoryHierarchy() {
        // Get root categories
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        return rootCategories.stream()
                .map(category -> {
                    CategoryResponseDTO dto = categoryMapper.convertToDTOWithoutChildren(category);
                    // Count actual children categories
                    List<Category> children = categoryRepository.findByParentId(category.getId());
                    dto.setChildrenCount(children.size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);

        // Cannot delete if has products
        if (!category.getProducts().isEmpty()) {
            return false;
        }

        // Cannot delete if has children
        if (categoryRepository.hasChildren(categoryId)) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoryPath(Long categoryId) {
        List<CategoryResponseDTO> path = new ArrayList<>();
        Category current = findCategoryById(categoryId);

        // Build path from current category to root
        while (current != null) {
            path.add(0, categoryMapper.convertToDTOWithoutChildren(current)); // Add at beginning
            current = current.getParent();
        }

        return path;
    }

    // Helper methods
    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục với ID: " + categoryId));
    }

    private void checkCategoryNameExistOnCreate(String name, Long parentId) {
        if (categoryRepository.existsByNameAndParentId(name, parentId)) {
            String parentInfo = parentId != null ? " trong cùng danh mục cha" : " ở cấp gốc";
            throw new IllegalArgumentException("Tên danh mục '" + name + "' đã tồn tại" + parentInfo);
        }
    }

    private void checkCategoryNameExistOnUpdate(String name, Long categoryId, Long parentId) {
        if (categoryRepository.existsByNameAndParentIdAndIdNot(name, parentId, categoryId)) {
            String parentInfo = parentId != null ? " trong cùng danh mục cha" : " ở cấp gốc";
            throw new IllegalArgumentException("Tên danh mục '" + name + "' đã được sử dụng" + parentInfo);
        }
    }

    private void checkCircularReference(Long categoryId, Long parentId) {
        if (categoryId.equals(parentId)) {
            throw new IllegalArgumentException("Danh mục không thể là cha của chính nó");
        }

        // Check if parentId is a descendant of categoryId
        Category parent = findCategoryById(parentId);
        Category current = parent.getParent();

        while (current != null) {
            if (current.getId().equals(categoryId)) {
                throw new IllegalArgumentException("Không thể tạo vòng lặp trong cây danh mục");
            }
            current = current.getParent();
        }
    }

    private String generateUniqueSlug(String baseSlug) {
        java.util.Set<String> existingSlugs = categoryRepository.findAll()
                .stream()
                .map(Category::getSlug)
                .collect(java.util.stream.Collectors.toSet());

        return SlugUtils.generateUniqueSlug(baseSlug, existingSlugs);
    }

    private String generateUniqueSlugForUpdate(String baseSlug, Long categoryId) {
        java.util.Set<String> existingSlugs = categoryRepository.findAll()
                .stream()
                .filter(category -> !category.getId().equals(categoryId)) // Exclude current category
                .map(Category::getSlug)
                .collect(java.util.stream.Collectors.toSet());

        return SlugUtils.generateUniqueSlug(baseSlug, existingSlugs);
    }

    private void checkSlugExistOnCreate(String slug) {
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug '" + slug + "' đã được sử dụng");
        }
    }

    private void checkSlugExistOnUpdate(String slug, Long categoryId) {
        if (categoryRepository.existsBySlugAndIdNot(slug, categoryId)) {
            throw new IllegalArgumentException("Slug '" + slug + "' đã được sử dụng");
        }
    }
}
