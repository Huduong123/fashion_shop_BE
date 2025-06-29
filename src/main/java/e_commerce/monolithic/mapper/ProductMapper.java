package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDTO convertToDTO (Product product) {
        if (product == null) {
            return null;
        }
        Long categoryId = (product.getCategory().getId() != null) ? product.getCategory().getId() : null;
        return  new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.isEnabled(),
                categoryId,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
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
