package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a FROM Authority a WHERE "
            + "(:username IS NULL OR a.user.username LIKE %:username%)"
            + "AND (:authority IS NULL OR a.authority LIKE %:authority%)")
    List<Authority> searchByUsernameAndAuthority(@Param("username") String username,
                                                 @Param("authority") String authority);
    // Tìm theo userID và quyền (tùy theo yêu cầu)
//    List<Authority> findByUserIdAndAuthorityContaining(Long userId, String authority);
}
