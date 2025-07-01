package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface UserProductService {

    Page<ProductResponseDTO> findAllVisibleProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Pageable pageable
    );
    ProductResponseDTO findVisibleProductById(Long productId);
}
