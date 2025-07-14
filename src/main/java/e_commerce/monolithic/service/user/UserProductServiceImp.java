package e_commerce.monolithic.service.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.enums.CategoryType;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import e_commerce.monolithic.repository.ProductRepository;
import e_commerce.monolithic.specification.ProductSpecification;

@Service
public class UserProductServiceImp implements UserProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductSpecification productSpecification;

    public UserProductServiceImp(ProductRepository productRepository, CategoryRepository categoryRepository,
            ProductMapper productMapper, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.productSpecification = productSpecification;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllVisibleProducts(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Long categoryId, Pageable pageable) {
        final Boolean isEnabled = true;

        Specification<Product> spec;

        if (categoryId != null) {
            // Check if category exists and get its type
            Category category = findCategoryById(categoryId);

            if (category.getType() == CategoryType.DROPDOWN) {
                // For DROPDOWN categories, include products from all child categories
                List<Long> categoryIds = getAllCategoryIds(categoryId);
                spec = productSpecification.findByCategoryIncludingChildren(name, minPrice, maxPrice,
                        isEnabled, categoryIds, null, null);
            } else {
                // For LINK categories, only get products from that specific category
                spec = productSpecification.findByExactCategory(name, minPrice, maxPrice,
                        isEnabled, categoryId, null, null);
            }
        } else {
            // No category filter, use original method
            spec = productSpecification.findByCriteria(name, minPrice, maxPrice, null, null,
                    isEnabled, categoryId, null, null);
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return productPage.map(productMapper::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findVisibleProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
        if (!product.isEnabled()) {
            throw new NotFoundException("Không tìm thấy sản phẩm với ID:  " + productId);
        }
        return productMapper.convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findVisibleProductsByCategory(Long categoryId, Pageable pageable) {
        // Validate category exists
        Category category = findCategoryById(categoryId);
        final Boolean isEnabled = true;

        Specification<Product> spec;

        if (category.getType() == CategoryType.DROPDOWN) {
            // For DROPDOWN categories, include products from all child categories
            List<Long> categoryIds = getAllCategoryIds(categoryId);
            spec = productSpecification.findByCategoryIncludingChildren(null, null, null,
                    isEnabled, categoryIds, null, null);
        } else {
            // For LINK categories, only get products from that specific category
            spec = productSpecification.findByExactCategory(null, null, null,
                    isEnabled, categoryId, null, null);
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return productPage.map(productMapper::convertToDTO);
    }

    /**
     * Get all category IDs including the given category and all its descendants
     * This method handles the recursive nature of category hierarchy
     */
    private List<Long> getAllCategoryIds(Long categoryId) {
        List<Long> categoryIds = new ArrayList<>();

        try {
            // Try to use recursive CTE query first (for MySQL 8.0+)
            categoryIds = categoryRepository.findAllDescendantCategoryIds(categoryId);
        } catch (Exception e) {
            // Fallback to application-level recursion if database doesn't support recursive
            // CTEs
            categoryIds = getAllCategoryIdsRecursive(categoryId);
        }

        return categoryIds;
    }

    /**
     * Recursive method to get all category IDs (fallback for databases without CTE
     * support)
     */
    private List<Long> getAllCategoryIdsRecursive(Long categoryId) {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId); // Add the category itself

        // Get direct children
        List<Long> directChildIds = categoryRepository.findDirectChildCategoryIds(categoryId);

        // Recursively get all descendants
        for (Long childId : directChildIds) {
            categoryIds.addAll(getAllCategoryIdsRecursive(childId));
        }

        return categoryIds;
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục với ID: " + categoryId));
    }
}
