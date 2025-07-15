package e_commerce.monolithic.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import e_commerce.monolithic.dto.admin.product.ProductVariantImageDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantImage;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductVariantImageMapper;
import e_commerce.monolithic.repository.ProductVariantImageRepository;
import e_commerce.monolithic.repository.ProductVariantRepository;

@Service
public class ProductVariantImageServiceImp implements ProductVariantImageService {

    private final ProductVariantImageRepository imageRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductVariantImageMapper imageMapper;

    public ProductVariantImageServiceImp(
            ProductVariantImageRepository imageRepository,
            ProductVariantRepository variantRepository,
            ProductVariantImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.variantRepository = variantRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantImageDTO> getImagesByVariantId(Long variantId) {
        validateVariantExists(variantId);
        return imageRepository.findByProductVariantIdOrderByDisplayOrderAsc(variantId)
                .stream()
                .map(imageMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductVariantImageDTO addImageToVariant(Long variantId, ProductVariantImageDTO imageDTO) {
        ProductVariant variant = findVariantById(variantId);

        // Set display order to max + 1
        Integer maxOrder = imageRepository.findMaxDisplayOrderByProductVariantId(variantId).orElse(-1);
        imageDTO.setDisplayOrder(maxOrder + 1);

        // If this is the first image, make it primary
        if (!imageRepository.existsByProductVariantIdAndIsPrimaryTrue(variantId)) {
            imageDTO.setPrimary(true);
        }

        ProductVariantImage image = imageMapper.convertToEntity(imageDTO);
        image.setProductVariant(variant);

        ProductVariantImage savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    @Override
    @Transactional
    public ProductVariantImageDTO updateImage(Long imageId, ProductVariantImageDTO imageDTO) {
        ProductVariantImage existingImage = findImageById(imageId);

        // If setting as primary, reset other primary images
        if (imageDTO.isPrimary() && !existingImage.isPrimary()) {
            imageRepository.resetPrimaryImageByProductVariantId(existingImage.getProductVariant().getId());
        }

        imageMapper.updateFromDTO(imageDTO, existingImage);
        ProductVariantImage savedImage = imageRepository.save(existingImage);
        return imageMapper.convertToDTO(savedImage);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteImage(Long imageId) {
        ProductVariantImage image = findImageById(imageId);
        Long variantId = image.getProductVariant().getId();
        boolean wasPrimary = image.isPrimary();

        imageRepository.delete(image);

        // If deleted image was primary, set another image as primary
        if (wasPrimary) {
            List<ProductVariantImage> remainingImages = imageRepository
                    .findByProductVariantIdOrderByDisplayOrderAsc(variantId);
            if (!remainingImages.isEmpty()) {
                ProductVariantImage newPrimary = remainingImages.get(0);
                newPrimary.setPrimary(true);
                imageRepository.save(newPrimary);
            }
        }

        return new ResponseMessageDTO(HttpStatus.OK, "Xóa ảnh thành công");
    }

    @Override
    @Transactional
    public List<ProductVariantImageDTO> setPrimaryImage(Long imageId) {
        ProductVariantImage image = findImageById(imageId);
        Long variantId = image.getProductVariant().getId();

        // Reset all primary images for this variant
        imageRepository.resetPrimaryImageByProductVariantId(variantId);

        // Set this image as primary
        image.setPrimary(true);
        imageRepository.save(image);

        return getImagesByVariantId(variantId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantImageDTO getPrimaryImage(Long variantId) {
        validateVariantExists(variantId);
        return imageRepository.findByProductVariantIdAndIsPrimaryTrue(variantId)
                .map(imageMapper::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void reorderImages(Long variantId, List<Long> imageIds) {
        validateVariantExists(variantId);

        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            ProductVariantImage image = findImageById(imageId);

            // Verify the image belongs to the variant
            if (!image.getProductVariant().getId().equals(variantId)) {
                throw new IllegalArgumentException("Ảnh không thuộc về variant này");
            }

            image.setDisplayOrder(i);
            imageRepository.save(image);
        }
    }

    @Override
    @Transactional
    public void deleteAllImagesByVariantId(Long variantId) {
        validateVariantExists(variantId);
        imageRepository.deleteByProductVariantId(variantId);
    }

    // Helper methods
    private ProductVariant findVariantById(Long variantId) {
        return variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy variant với ID: " + variantId));
    }

    private ProductVariantImage findImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh với ID: " + imageId));
    }

    private void validateVariantExists(Long variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new NotFoundException("Không tìm thấy variant với ID: " + variantId);
        }
    }
}