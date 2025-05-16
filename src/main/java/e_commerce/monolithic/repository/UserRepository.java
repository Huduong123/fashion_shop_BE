package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Tìm User theo username
    Optional<User> findByUsername(String username);

    // Tìm User theo email
    Optional<User> findByEmail(String email);

    // Kiểm tra xem username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Kiểm tra xem email đã tồn tại chưa
    boolean existsByEmail(String email);

    //Kiểm tra xem phone tồn tại chưa
    boolean existsByPhone(String phone);

}
