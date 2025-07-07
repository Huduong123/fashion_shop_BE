package e_commerce.monolithic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.monolithic.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // Tìm User theo username và eager load authorities
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    // Tìm User theo id và eager load authorities
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE u.id = :id")
    Optional<User> findByIdWithAuthorities(@Param("id") Long id);

    // Lấy tất cả User và eager load authorities
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.authorities ORDER BY u.createdAt DESC")
    List<User> findAllWithAuthorities();

    // Tìm User theo email
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    // Kiểm tra xem username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Kiểm tra xem email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Kiểm tra xem phone tồn tại chưa
    boolean existsByPhone(String phone);

}
