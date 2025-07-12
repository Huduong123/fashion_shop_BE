package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.ProductVariantSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantSizeRepository extends JpaRepository<ProductVariantSize, Long> {

    /**
     * Find all sizes for a specific product variant
     */
    List<ProductVariantSize> findByProductVariantId(Long productVariantId);

    /**
     * Find a specific size for a product variant
     */
    Optional<ProductVariantSize> findByProductVariantIdAndSizeId(Long productVariantId, Long sizeId);

    /**
     * Check if a size exists for a product variant
     */
    boolean existsByProductVariantIdAndSizeId(Long productVariantId, Long sizeId);

    /**
     * Find all sizes for product variants of a specific product
     */
    @Query("SELECT pvs FROM ProductVariantSize pvs " +
           "WHERE pvs.productVariant.product.id = :productId")
    List<ProductVariantSize> findByProductId(@Param("productId") Long productId);

    /**
     * Find all available sizes (quantity > 0) for a product variant
     */
    @Query("SELECT pvs FROM ProductVariantSize pvs " +
           "WHERE pvs.productVariant.id = :productVariantId AND pvs.quantity > 0")
    List<ProductVariantSize> findAvailableByProductVariantId(@Param("productVariantId") Long productVariantId);

    /**
     * Delete all sizes for a product variant
     */
    void deleteByProductVariantId(Long productVariantId);
} 