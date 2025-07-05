package e_commerce.monolithic.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

public interface ProductService {

    List<ProductResponseDTO> findAll(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt);

    ProductResponseDTO findById(Long productId);

    ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO);

    ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO);

    ResponseMessageDTO deleteProduct(Long productId);

    List<ProductResponseDTO> findProductsByCategory(Long categoryId);

    Page<ProductResponseDTO> findProductsByCategoryWithPagination(Long categoryId, Pageable pageable);
}
