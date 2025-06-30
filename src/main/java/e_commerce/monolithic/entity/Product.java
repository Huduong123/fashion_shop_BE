package e_commerce.monolithic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@NamedEntityGraph(
        name = "Product.withVariants",
        attributeNodes = @NamedAttributeNode("productVariants")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"category", "productVariants"})
public class Product  extends  BaseEntity{

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",
    cascade = CascadeType.ALL,orphanRemoval = true,
    fetch = FetchType.LAZY)
    private Set<ProductVariant> productVariants = new HashSet<>();

}
