package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // Kiểm tra xem màu có đang được sử dụng bởi variant nào không
    @Query("SELECT COUNT(pv) > 0 FROM ProductVariant pv WHERE pv.color.id = :colorId")
    boolean existsByColorId(@Param("colorId") Long colorId);

    // Kiểm tra xem size có đang được sử dụng bởi variant nào không
    @Query("SELECT COUNT(pvs) > 0 FROM ProductVariantSize pvs WHERE pvs.size.id = :sizeId")
    boolean existsBySizeId(@Param("sizeId") Long sizeId);

    // Đếm số sản phẩm sử dụng màu
    @Query("SELECT COUNT(DISTINCT pv.product) FROM ProductVariant pv WHERE pv.color.id = :colorId")
    long countProductsByColorId(@Param("colorId") Long colorId);

    // Đếm số sản phẩm sử dụng size
    @Query("SELECT COUNT(DISTINCT pvs.productVariant.product) FROM ProductVariantSize pvs WHERE pvs.size.id = :sizeId")
    long countProductsBySizeId(@Param("sizeId") Long sizeId);
}
