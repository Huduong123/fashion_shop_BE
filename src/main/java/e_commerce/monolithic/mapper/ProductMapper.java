package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ProductVariantMapper productVariantMapper;

    public ProductMapper(ProductVariantMapper productVariantMapper) {
        this.productVariantMapper = productVariantMapper;
    }

    public ProductResponseDTO convertToDTO (Product product) {
        if (product == null) {
            return null;
        }
        Long categoryId = (product.getCategory().getId() != null) ? product.getCategory().getId() : null;

        var productVariants = product.getProductVariants()
                .stream()
                .map(productVariantMapper::convertToDTO)
                .collect(Collectors.toList());

        var responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setEnabled(product.isEnabled());
        responseDTO.setCategoryId(categoryId);
        responseDTO.setCreatedAt(product.getCreatedAt());
        responseDTO.setUpdatedAt(product.getUpdatedAt());
        responseDTO.setProductVariants(productVariants);

        return responseDTO;
    }

    public Product convertCreateDtoToEntity (ProductCreateDTO productCreateDTO, Category category) {
        if (productCreateDTO == null) {
            return null;
        }

        return Product.builder()
                .name(productCreateDTO.getName())
                .description(productCreateDTO.getDescription())
                .enabled(productCreateDTO.getEnabled())
                .category(category)
                .productVariants(new HashSet<>())
                .build();
    }

    public void convertUpdateDtoToEntity (ProductUpdateDTO productUpdateDTO, Product existingProduct, Category category) {
        if (productUpdateDTO == null || existingProduct == null) {
            return;
        }

        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setDescription(productUpdateDTO.getDescription());

        existingProduct.setEnabled(productUpdateDTO.getEnabled());
        existingProduct.setCategory(category);
    }
}
