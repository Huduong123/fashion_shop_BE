package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.product.ProductVariantSizeCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

import java.util.List;

public interface ProductVariantSizeService {

    /**
     * Get all sizes for a specific product variant
     */
    List<ProductVariantSizeResponseDTO> getSizesByProductVariantId(Long productVariantId);

    /**
     * Create a new size for a product variant
     */
    ProductVariantSizeResponseDTO createSize(Long productVariantId, ProductVariantSizeCreateDTO createDTO);

    /**
     * Update an existing product variant size
     */
    ProductVariantSizeResponseDTO updateSize(Long id, ProductVariantSizeUpdateDTO updateDTO);

    /**
     * Delete a product variant size
     */
    ResponseMessageDTO deleteSize(Long id);

    /**
     * Get a specific size by ID
     */
    ProductVariantSizeResponseDTO getSizeById(Long id);

    /**
     * Check if a size exists for a product variant
     */
    boolean existsByProductVariantIdAndSizeId(Long productVariantId, Long sizeId);
}