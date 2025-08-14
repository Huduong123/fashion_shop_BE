package e_commerce.monolithic.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.monolithic.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
        /**
         * Lấy danh sách đơn hàng của user, sắp xếp theo ngày tạo mới nhất.
         * Sử dụng LEFT JOIN FETCH để tải tất cả các thông tin liên quan (items, variants, products,...)
         * cùng lúc, tránh N+1 query.
         */
        @Query(value = "SELECT o FROM Order o " +
                        "LEFT JOIN FETCH o.orderItems oi " +
                        "LEFT JOIN FETCH oi.productVariant pv " +
                        "LEFT JOIN FETCH pv.product p " +
                        "LEFT JOIN FETCH pv.color c " +
                        "LEFT JOIN FETCH oi.size s " +
                        "WHERE o.user.id = :userId",
               countQuery = "SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
        Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);
        // Lấy chi tiết một đơn hàng, đảm bảo nó thuộc về đúng user
        // JOIN FETCH tất cả các thông tin cần thiết để hiển thị chi tiết
        @Query("SELECT o FROM Order o " +
                        "JOIN FETCH o.orderItems oi " +
                        "JOIN FETCH oi.productVariant pv " +
                        "JOIN FETCH pv.product p " +
                        "JOIN FETCH pv.color c " +
                        "JOIN FETCH oi.size s " +
                        "WHERE o.id = :orderId AND o.user.id = :userId")
        Optional<Order> findByIdAndUserIdWithDetails(@Param("orderId") Long orderId,
                        @Param("userId") Long userId);
}
