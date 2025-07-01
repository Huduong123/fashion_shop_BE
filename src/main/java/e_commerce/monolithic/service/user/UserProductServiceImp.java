package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.repository.ProductRepository;
import e_commerce.monolithic.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserProductServiceImp implements UserProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductSpecification productSpecification;

    public UserProductServiceImp(ProductRepository productRepository, ProductMapper productMapper, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSpecification = productSpecification;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllVisibleProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, Pageable pageable) {
        final Boolean isEnabled = true;

        Specification<Product> spec = productSpecification.findByCriteria(name, minPrice, maxPrice, null, null, isEnabled, categoryId, null, null);

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return  productPage.map(productMapper::convertToDTO);
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
}
