package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.CartItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * Tải tất cả các mục trong giỏ hàng của một người dùng.
     * Sử dụng JOIN FETCH để giải quyết vấn đề N+1 query bằng cách tải tất cả
     * thông tin cần thiết (product variant, product, color, size) trong một câu truy vấn duy nhất.
     */
    @Query("SELECT ci FROM CartItem ci " +
    "JOIN FETCH ci.productVariant pv " +
    "JOIN FETCH pv.product p " +
    "JOIN FETCH pv.color c "+
    "JOIN FETCH pv.size s " +
    "WHERE ci.user.id = :userId "+
    "ORDER BY ci.updatedAt DESC")
    List<CartItem> findByUserIdWithDetails(@Param("userId") Long userId);

    /**
     * Tìm một mục trong giỏ hàng dựa trên user_id và product_variant_id.
     * Hữu ích để kiểm tra xem sản phẩm đã có trong giỏ hàng chưa.
     */
    Optional<CartItem> findByUserIdAndProductVariantId(Long userId, Long productVariantId);

    /**
     * Tải các mục trong giỏ hàng của một người dùng, có phân trang và sắp xếp.
     * Dùng để lấy các sản phẩm thêm gần nhất.
     */
    @Query("SELECT ci FROM CartItem ci " +
    "JOIN FETCH ci.productVariant pv " +
    "JOIN FETCH pv.product p " +
    "JOIN FETCH pv.color c " +
    "JOIN FETCH pv.size s " +
    "WHERE ci.user.id = :userId")
    List<CartItem> findRecentByUserIdWithDetails(@Param("userId") Long userId, Pageable pageable);

    /**
     * Tìm một mục trong giỏ hàng bằng ID của nó và ID của người dùng.
     * Điều này đảm bảo rằng người dùng chỉ có thể thao tác với các mục trong giỏ hàng của chính họ.
     */
    Optional<CartItem> findByIdAndUserId(Long id, Long userId);


    /**
     * Xóa tất cả các mục trong giỏ hàng của một người dùng cụ thể.
     * Sử dụng @Modifying và @Transactional vì đây là một câu lệnh DML (thay đổi dữ liệu).
     * @param userId ID của người dùng.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
