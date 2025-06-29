package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
