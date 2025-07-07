package e_commerce.monolithic.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import e_commerce.monolithic.entity.enums.ProductVariantStatus;
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
                                "product_id", "color_id", "size_id"
                }, name = "uq_product_variant")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "product", "color", "size", "orderItems" })
public class ProductVariant extends BaseEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "color_id", nullable = false)
        private Color color;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "size_id", nullable = true)
        private Size size;

        @Column(name = "price", nullable = false, precision = 10, scale = 2)
        private BigDecimal price;

        @Column(name = "quantity", nullable = false)
        private int quantity;

        @Column(name = "image_url")
        private String imageUrl;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        @Builder.Default
        private ProductVariantStatus status = ProductVariantStatus.ACTIVE;

        @OneToMany(mappedBy = "productVariant", fetch = FetchType.LAZY)
        private Set<OrderItem> orderItems = new HashSet<>();

}
