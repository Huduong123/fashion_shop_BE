package e_commerce.monolithic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.monolithic.entity.ProductVariantImage;

@Repository
public interface ProductVariantImageRepository extends JpaRepository<ProductVariantImage, Long> {

    // Tìm tất cả ảnh của một variant, sắp xếp theo displayOrder
    List<ProductVariantImage> findByProductVariantIdOrderByDisplayOrderAsc(Long productVariantId);

    // Tìm ảnh chính của một variant
    Optional<ProductVariantImage> findByProductVariantIdAndIsPrimaryTrue(Long productVariantId);

    // Đếm số ảnh của một variant
    long countByProductVariantId(Long productVariantId);

    // Xóa tất cả ảnh của một variant
    @Modifying
    @Query("DELETE FROM ProductVariantImage pvi WHERE pvi.productVariant.id = :productVariantId")
    void deleteByProductVariantId(@Param("productVariantId") Long productVariantId);

    // Tìm ảnh có displayOrder lớn nhất của một variant
    @Query("SELECT MAX(pvi.displayOrder) FROM ProductVariantImage pvi WHERE pvi.productVariant.id = :productVariantId")
    Optional<Integer> findMaxDisplayOrderByProductVariantId(@Param("productVariantId") Long productVariantId);

    // Reset tất cả ảnh của variant về không phải primary
    @Modifying
    @Query("UPDATE ProductVariantImage pvi SET pvi.isPrimary = false WHERE pvi.productVariant.id = :productVariantId")
    void resetPrimaryImageByProductVariantId(@Param("productVariantId") Long productVariantId);

    // Kiểm tra xem variant đã có ảnh chính chưa
    boolean existsByProductVariantIdAndIsPrimaryTrue(Long productVariantId);
} 