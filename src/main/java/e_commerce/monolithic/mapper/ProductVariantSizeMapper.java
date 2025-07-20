package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.product.ProductVariantSizeCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeUpdateDTO;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantSize;
import e_commerce.monolithic.entity.Size;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantSizeMapper {

    /**
     * Convert ProductVariantSize entity to ResponseDTO.
     */
    public ProductVariantSizeResponseDTO convertToDTO(ProductVariantSize productVariantSize) {
        if (productVariantSize == null) {
            return null;
        }

        // --- THIS IS THE FINAL CORRECTED LINE ---
        // It now passes 5 arguments to match the 5 fields in your DTO.
        return new ProductVariantSizeResponseDTO(
            productVariantSize.getSize().getId(),    // Matches the 'id' field
            productVariantSize.getSize().getName(),  // Matches the 'sizeName' field
            productVariantSize.getPrice(),           // Matches the 'price' field
            productVariantSize.getQuantity(),        // Matches the 'quantity' field
            productVariantSize.isAvailable()         // Matches the 'available' field
        );
    }

    /**
     * Convert list of ProductVariantSize entities to ResponseDTOs
     */
    public List<ProductVariantSizeResponseDTO> convertToDTO(List<ProductVariantSize> productVariantSizes) {
        return productVariantSizes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert CreateDTO to ProductVariantSize entity
     */
    public ProductVariantSize convertCreateDtoToEntity(ProductVariantSizeCreateDTO createDTO, 
                                                      ProductVariant productVariant, 
                                                      Size size) {
        if (createDTO == null) {
            return null;
        }

        return ProductVariantSize.builder()
                .productVariant(productVariant)
                .size(size)
                .price(createDTO.getPrice())
                .quantity(createDTO.getQuantity())
                .build();
    }

    /**
     * Convert UpdateDTO to ProductVariantSize entity for new entity
     */
    public ProductVariantSize convertUpdateDtoToNewEntity(ProductVariantSizeUpdateDTO updateDTO, 
                                                         ProductVariant productVariant, 
                                                         Size size) {
        if (updateDTO == null) {
            return null;
        }

        return ProductVariantSize.builder()
                .productVariant(productVariant)
                .size(size)
                .price(updateDTO.getPrice())
                .quantity(updateDTO.getQuantity())
                .build();
    }

    /**
     * Update existing ProductVariantSize entity from UpdateDTO
     */
    public void convertUpdateDtoToEntity(ProductVariantSizeUpdateDTO updateDTO, 
                                        ProductVariantSize existingEntity, 
                                        Size size) {
        if (updateDTO == null || existingEntity == null) {
            return;
        }

        existingEntity.setSize(size);
        existingEntity.setPrice(updateDTO.getPrice());
        existingEntity.setQuantity(updateDTO.getQuantity());
    }
}