package e_commerce.monolithic.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.monolithic.dto.admin.product.ProductVariantImageDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.service.admin.ProductVariantImageService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/product-variants")
public class ProductVariantImageController {

    private final ProductVariantImageService imageService;

    public ProductVariantImageController(ProductVariantImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Get all images for a specific variant
     * GET /api/admin/product-variants/{variantId}/images
     */
    @GetMapping("/{variantId}/images")
    public ResponseEntity<List<ProductVariantImageDTO>> getVariantImages(@PathVariable Long variantId) {
        List<ProductVariantImageDTO> images = imageService.getImagesByVariantId(variantId);
        return ResponseEntity.ok(images);
    }

    /**
     * Get primary image for a specific variant
     * GET /api/admin/product-variants/{variantId}/images/primary
     */
    @GetMapping("/{variantId}/images/primary")
    public ResponseEntity<ProductVariantImageDTO> getPrimaryImage(@PathVariable Long variantId) {
        ProductVariantImageDTO primaryImage = imageService.getPrimaryImage(variantId);
        if (primaryImage != null) {
            return ResponseEntity.ok(primaryImage);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Add new image to a variant
     * POST /api/admin/product-variants/{variantId}/images
     */
    @PostMapping("/{variantId}/images")
    public ResponseEntity<ProductVariantImageDTO> addImageToVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantImageDTO imageDTO) {
        ProductVariantImageDTO savedImage = imageService.addImageToVariant(variantId, imageDTO);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }

    /**
     * Update an existing image
     * PUT /api/admin/product-variants/images/{imageId}
     */
    @PutMapping("/images/{imageId}")
    public ResponseEntity<ProductVariantImageDTO> updateImage(
            @PathVariable Long imageId,
            @Valid @RequestBody ProductVariantImageDTO imageDTO) {
        ProductVariantImageDTO updatedImage = imageService.updateImage(imageId, imageDTO);
        return ResponseEntity.ok(updatedImage);
    }

    /**
     * Delete an image
     * DELETE /api/admin/product-variants/images/{imageId}
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ResponseMessageDTO> deleteImage(@PathVariable Long imageId) {
        ResponseMessageDTO response = imageService.deleteImage(imageId);
        return ResponseEntity.ok(response);
    }

    /**
     * Set an image as primary
     * PUT /api/admin/product-variants/images/{imageId}/set-primary
     */
    @PutMapping("/images/{imageId}/set-primary")
    public ResponseEntity<List<ProductVariantImageDTO>> setPrimaryImage(@PathVariable Long imageId) {
        List<ProductVariantImageDTO> updatedImages = imageService.setPrimaryImage(imageId);
        return ResponseEntity.ok(updatedImages);
     }

    /**
     * Reorder images for a variant
     * PUT /api/admin/product-variants/{variantId}/images/reorder
     */
    @PutMapping("/{variantId}/images/reorder")
    public ResponseEntity<ResponseMessageDTO> reorderImages(
            @PathVariable Long variantId,
            @RequestBody List<Long> imageIds) {
        imageService.reorderImages(variantId, imageIds);
        return ResponseEntity.ok(new ResponseMessageDTO(HttpStatus.OK, "Sắp xếp lại ảnh thành công"));
    }

    /**
     * Delete all images of a variant
     * DELETE /api/admin/product-variants/{variantId}/images
     */
    @DeleteMapping("/{variantId}/images")
    public ResponseEntity<ResponseMessageDTO> deleteAllVariantImages(@PathVariable Long variantId) {
        imageService.deleteAllImagesByVariantId(variantId);
        return ResponseEntity.ok(new ResponseMessageDTO(HttpStatus.OK, "Xóa tất cả ảnh thành công"));
    }
} 