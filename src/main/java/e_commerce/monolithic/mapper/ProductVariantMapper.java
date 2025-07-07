package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.product.ProductVariantCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantUpdateDTO;
import e_commerce.monolithic.entity.Color;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariantResponseDTO convertToDTO(ProductVariant productVariant) {
        if (productVariant == null) {
            return null;
        }
        ProductVariantResponseDTO productVariantResponseDTO = new ProductVariantResponseDTO();
        productVariantResponseDTO.setId(productVariant.getId());
        productVariantResponseDTO.setPrice(productVariant.getPrice());
        productVariantResponseDTO.setQuantity(productVariant.getQuantity());
        productVariantResponseDTO.setImageUrl(productVariant.getImageUrl());
        productVariantResponseDTO.setStatus(productVariant.getStatus());
        if (productVariant.getColor() != null) {
            productVariantResponseDTO.setColorId(productVariant.getColor().getId());
            productVariantResponseDTO.setColorName(productVariant.getColor().getName());
        }
        if (productVariant.getSize() != null) {
            productVariantResponseDTO.setSizeId(productVariant.getSize().getId());
            productVariantResponseDTO.setSizeName(productVariant.getSize().getName());
        }
        return productVariantResponseDTO;
    }

    public ProductVariant convertCreateDtoToEntity(ProductVariantCreateDTO productVariantCreateDTO, Color color,
            Size size) {
        if (productVariantCreateDTO == null) {
            return null;
        }

        return ProductVariant.builder()
                .color(color)
                .size(size)
                .price(productVariantCreateDTO.getPrice())
                .quantity(productVariantCreateDTO.getQuantity())
                .imageUrl(productVariantCreateDTO.getImageUrl())
                .status(productVariantCreateDTO.getStatus() != null ? productVariantCreateDTO.getStatus()
                        : ProductVariantStatus.ACTIVE)
                .build();
    }

    public ProductVariant convertUpdateDtoToNewEntity(ProductVariantUpdateDTO dto, Color color, Size size) {
        if (dto == null) {
            return null;
        }
        return ProductVariant.builder()
                .color(color)
                .size(size)
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .imageUrl(dto.getImageUrl())
                .status(dto.getStatus() != null ? dto.getStatus() : ProductVariantStatus.ACTIVE)
                .build();
    }
}
