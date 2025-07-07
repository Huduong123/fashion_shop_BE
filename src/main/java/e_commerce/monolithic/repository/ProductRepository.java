package e_commerce.monolithic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.monolithic.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(value = "Product.withVariants")
    List<Product> findAll(Specification<Product> spec);

    @Override
    @EntityGraph(value = "Product.withVariants")
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @EntityGraph(value = "Product.withVariants")
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithVariants(@Param("id") Long id);

    // Check if product has reviews
    @Query("SELECT COUNT(r) > 0 FROM Product p JOIN p.reviews r WHERE p.id = :productId")
    boolean hasReviews(@Param("productId") Long productId);

    // Check if product has order items through its variants
    @Query("SELECT COUNT(oi) > 0 FROM Product p JOIN p.productVariants pv JOIN pv.orderItems oi WHERE p.id = :productId")
    boolean hasOrderItems(@Param("productId") Long productId);
}
