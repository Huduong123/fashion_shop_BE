package e_commerce.monolithic.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.repository.ProductRepository;

@Component
public class ProductMapper {

    private final ProductVariantMapper productVariantMapper;
    private final ProductRepository productRepository;

    public ProductMapper(ProductVariantMapper productVariantMapper, ProductRepository productRepository) {
        this.productVariantMapper = productVariantMapper;
        this.productRepository = productRepository;
    }

    public ProductResponseDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }
        Long categoryId = (product.getCategory() != null && product.getCategory().getId() != null)
                ? product.getCategory().getId()
                : null;
        String categoryName = (product.getCategory() != null && product.getCategory().getName() != null)
                ? product.getCategory().getName()
                : null;

        var productVariants = product.getProductVariants()
                .stream()
                .map(productVariantMapper::convertToDTO)
                .collect(Collectors.toList());

        // Check if product can be deleted (no reviews and no order items)
        boolean canDelete = !productRepository.hasReviews(product.getId()) &&
                !productRepository.hasOrderItems(product.getId());

        var responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setEnabled(product.isEnabled());
        responseDTO.setCategoryId(categoryId);
        responseDTO.setCategoryName(categoryName);
        responseDTO.setCreatedAt(product.getCreatedAt());
        responseDTO.setUpdatedAt(product.getUpdatedAt());
        responseDTO.setProductVariants(productVariants);
        responseDTO.setCanDelete(canDelete);

        return responseDTO;
    }

    public Product convertCreateDtoToEntity(ProductCreateDTO productCreateDTO, Category category) {
        if (productCreateDTO == null) {
            return null;
        }

        return Product.builder()
                .name(productCreateDTO.getName())
                .description(productCreateDTO.getDescription())
                .enabled(productCreateDTO.getEnabled())
                .category(category)
                .productVariants(new ArrayList<>())
                .build();
    }

    public void convertUpdateDtoToEntity(ProductUpdateDTO productUpdateDTO, Product existingProduct,
            Category category) {
        if (productUpdateDTO == null || existingProduct == null) {
            return;
        }

        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setDescription(productUpdateDTO.getDescription());

        existingProduct.setEnabled(productUpdateDTO.getEnabled());
        existingProduct.setCategory(category);
    }
}
