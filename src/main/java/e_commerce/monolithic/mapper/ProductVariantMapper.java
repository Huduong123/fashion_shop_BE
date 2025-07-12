package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.product.ProductVariantCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantImageDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantUpdateDTO;
import e_commerce.monolithic.entity.Color;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantImage;
import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ProductVariantMapper {

    private final ProductVariantImageMapper imageMapper;
    private final ProductVariantSizeMapper sizeMapper;

    public ProductVariantMapper(ProductVariantImageMapper imageMapper, ProductVariantSizeMapper sizeMapper) {
        this.imageMapper = imageMapper;
        this.sizeMapper = sizeMapper;
    }

    public ProductVariantResponseDTO convertToDTO(ProductVariant productVariant) {
        if (productVariant == null) {
            return null;
        }
        ProductVariantResponseDTO productVariantResponseDTO = new ProductVariantResponseDTO();
        productVariantResponseDTO.setId(productVariant.getId());
        productVariantResponseDTO.setImageUrl(productVariant.getImageUrl());
        productVariantResponseDTO.setStatus(productVariant.getStatus());

        if (productVariant.getColor() != null) {
            productVariantResponseDTO.setColorId(productVariant.getColor().getId());
            productVariantResponseDTO.setColorName(productVariant.getColor().getName());
        }

        // Map sizes
        if (productVariant.getProductVariantSizes() != null) {
            productVariantResponseDTO.setSizes(
                    sizeMapper.convertToDTO(productVariant.getProductVariantSizes()));
        } else {
            productVariantResponseDTO.setSizes(new ArrayList<>());
        }

        // Set calculated fields
        productVariantResponseDTO.setMinPrice(productVariant.getMinPrice());
        productVariantResponseDTO.setMaxPrice(productVariant.getMaxPrice());
        productVariantResponseDTO.setTotalQuantity(productVariant.getTotalQuantity());
        productVariantResponseDTO.setAvailable(productVariant.isAvailable());

        // Map images
        if (productVariant.getImages() != null) {
            productVariantResponseDTO.setImages(
                    productVariant.getImages().stream()
                            .map(imageMapper::convertToDTO)
                            .collect(Collectors.toList()));
        } else {
            productVariantResponseDTO.setImages(new ArrayList<>());
        }

        return productVariantResponseDTO;
    }

    public ProductVariant convertCreateDtoToEntity(ProductVariantCreateDTO productVariantCreateDTO, Color color) {
        if (productVariantCreateDTO == null) {
            return null;
        }

        ProductVariant variant = ProductVariant.builder()
                .color(color)
                .imageUrl(productVariantCreateDTO.getImageUrl())
                .status(productVariantCreateDTO.getStatus() != null ? productVariantCreateDTO.getStatus()
                        : ProductVariantStatus.ACTIVE)
                .build();

        // Handle images
        if (productVariantCreateDTO.getImages() != null && !productVariantCreateDTO.getImages().isEmpty()) {
            for (int i = 0; i < productVariantCreateDTO.getImages().size(); i++) {
                ProductVariantImageDTO imageDTO = productVariantCreateDTO.getImages().get(i);
                ProductVariantImage image = imageMapper.convertToEntity(imageDTO);
                image.setDisplayOrder(i);
                image.setProductVariant(variant);

                // Set first image as primary if no primary is specified
                if (i == 0
                        && productVariantCreateDTO.getImages().stream().noneMatch(ProductVariantImageDTO::isPrimary)) {
                    image.setPrimary(true);
                }

                variant.addImage(image);
            }
        }

        return variant;
    }

    public ProductVariant convertUpdateDtoToNewEntity(ProductVariantUpdateDTO dto, Color color) {
        if (dto == null) {
            return null;
        }

        ProductVariant variant = ProductVariant.builder()
                .color(color)
                .imageUrl(dto.getImageUrl())
                .status(dto.getStatus() != null ? dto.getStatus() : ProductVariantStatus.ACTIVE)
                .build();

        // Handle images
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (int i = 0; i < dto.getImages().size(); i++) {
                ProductVariantImageDTO imageDTO = dto.getImages().get(i);
                ProductVariantImage image = imageMapper.convertToEntity(imageDTO);
                image.setDisplayOrder(i);
                image.setProductVariant(variant);

                // Set first image as primary if no primary is specified
                if (i == 0 && dto.getImages().stream().noneMatch(ProductVariantImageDTO::isPrimary)) {
                    image.setPrimary(true);
                }

                variant.addImage(image);
            }
        }

        return variant;
    }
}
