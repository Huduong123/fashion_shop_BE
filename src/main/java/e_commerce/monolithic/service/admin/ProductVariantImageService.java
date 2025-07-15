package e_commerce.monolithic.service.admin;

import java.util.List;

import e_commerce.monolithic.dto.admin.product.ProductVariantImageDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

public interface ProductVariantImageService {

    List<ProductVariantImageDTO> getImagesByVariantId(Long variantId);

    ProductVariantImageDTO addImageToVariant(Long variantId, ProductVariantImageDTO imageDTO);

    ProductVariantImageDTO updateImage(Long imageId, ProductVariantImageDTO imageDTO);

    ResponseMessageDTO deleteImage(Long imageId);

    List<ProductVariantImageDTO> setPrimaryImage(Long imageId);

    ProductVariantImageDTO getPrimaryImage(Long variantId);

    void reorderImages(Long variantId, List<Long> imageIds);

    void deleteAllImagesByVariantId(Long variantId);
} 