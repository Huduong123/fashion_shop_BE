package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(value = "Product.withVariants")
    List<Product> findAll(Specification<Product> spec);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
