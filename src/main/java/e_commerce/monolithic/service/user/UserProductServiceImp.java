package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import e_commerce.monolithic.repository.ProductRepository;
import e_commerce.monolithic.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

        Specification<Product> spec = productSpecification.findByCriteria(name, minPrice, maxPrice, null, null,
                isEnabled, categoryId, null, null);

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
        findCategoryById(categoryId);
        
        final Boolean isEnabled = true;

        // Use existing specification with only categoryId and enabled filters
        Specification<Product> spec = productSpecification.findByCriteria(null, null, null, null, null, isEnabled,
                categoryId, null, null);

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::convertToDTO);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục với ID: " + categoryId));
    }
}
