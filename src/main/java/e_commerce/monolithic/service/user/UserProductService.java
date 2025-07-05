package e_commerce.monolithic.service.user;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;

public interface UserProductService {

    Page<ProductResponseDTO> findAllVisibleProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Pageable pageable);

    ProductResponseDTO findVisibleProductById(Long productId);

    Page<ProductResponseDTO> findVisibleProductsByCategory(Long categoryId, Pageable pageable);
}
