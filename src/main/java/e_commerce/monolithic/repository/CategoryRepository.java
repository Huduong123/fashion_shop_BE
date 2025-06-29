package e_commerce.monolithic.repository;

import e_commerce.monolithic.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
