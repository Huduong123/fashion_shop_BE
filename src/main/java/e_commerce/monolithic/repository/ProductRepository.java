package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
