package e_commerce.monolithic.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import e_commerce.monolithic.entity.enums.ProductVariantStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_variants", uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                                "product_id", "color_id"
                }, name = "uq_product_variant")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "product", "color", "orderItems", "images", "productVariantSizes" })
public class ProductVariant extends BaseEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "color_id", nullable = false)
        private Color color;

        @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        @Builder.Default
        private List<ProductVariantSize> productVariantSizes = new ArrayList<>();

        @Column(name = "image_url")
        private String imageUrl; // Keep for backward compatibility

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        @Builder.Default
        private ProductVariantStatus status = ProductVariantStatus.ACTIVE;

        @OneToMany(mappedBy = "productVariant", fetch = FetchType.LAZY)
        private Set<OrderItem> orderItems = new HashSet<>();

        @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        @Builder.Default
        private List<ProductVariantImage> images = new ArrayList<>();

        // Helper methods for managing images
        public void addImage(ProductVariantImage image) {
                images.add(image);
                image.setProductVariant(this);
        }

        public void removeImage(ProductVariantImage image) {
                images.remove(image);
                image.setProductVariant(null);
        }

        // Get primary image
        public ProductVariantImage getPrimaryImage() {
                return images.stream()
                                .filter(ProductVariantImage::isPrimary)
                                .findFirst()
                                .orElse(images.isEmpty() ? null : images.get(0));
        }

        // Get primary image URL with fallback to legacy imageUrl
        public String getPrimaryImageUrl() {
                ProductVariantImage primaryImage = getPrimaryImage();
                if (primaryImage != null) {
                        return primaryImage.getImageUrl();
                }
                return imageUrl; // Fallback to legacy field
        }

        // Helper methods for managing ProductVariantSizes
        public void addProductVariantSize(ProductVariantSize productVariantSize) {
                productVariantSizes.add(productVariantSize);
                productVariantSize.setProductVariant(this);
        }

        public void removeProductVariantSize(ProductVariantSize productVariantSize) {
                productVariantSizes.remove(productVariantSize);
                productVariantSize.setProductVariant(null);
        }

        // Get total quantity across all sizes
        public int getTotalQuantity() {
                return productVariantSizes.stream()
                                .mapToInt(ProductVariantSize::getQuantity)
                                .sum();
        }

        // Get minimum price across all sizes
        public BigDecimal getMinPrice() {
                return productVariantSizes.stream()
                                .map(ProductVariantSize::getPrice)
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);
        }

        // Get maximum price across all sizes
        public BigDecimal getMaxPrice() {
                return productVariantSizes.stream()
                                .map(ProductVariantSize::getPrice)
                                .max(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);
        }

        // Check if variant is available (has at least one size with quantity > 0)
        public boolean isAvailable() {
                return productVariantSizes.stream()
                                .anyMatch(ProductVariantSize::isAvailable);
        }
}
