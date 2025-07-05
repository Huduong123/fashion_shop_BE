package e_commerce.monolithic.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString(exclude = { "product", "color", "size" })
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

}
