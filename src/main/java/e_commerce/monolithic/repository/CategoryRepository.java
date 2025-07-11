package e_commerce.monolithic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.monolithic.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    // Find root categories (categories without parent)
    List<Category> findByParentIsNull();

    // Find children of a specific parent
    List<Category> findByParentId(Long parentId);

    // Find all children of a category (including nested children)
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findDirectChildrenByParentId(@Param("parentId") Long parentId);

    // Check if category name exists within the same parent
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND " +
           "((:parentId IS NULL AND c.parent IS NULL) OR c.parent.id = :parentId)")
    boolean existsByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    // Check if category name exists within the same parent, excluding current category
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.id != :categoryId AND " +
           "((:parentId IS NULL AND c.parent IS NULL) OR c.parent.id = :parentId)")
    boolean existsByNameAndParentIdAndIdNot(@Param("name") String name, 
                                           @Param("parentId") Long parentId, 
                                           @Param("categoryId") Long categoryId);

    // Check if a category has children
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.parent.id = :categoryId")
    boolean hasChildren(@Param("categoryId") Long categoryId);

    // Count total products in category and its children
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId OR " +
           "p.category.id IN (SELECT c.id FROM Category c WHERE c.parent.id = :categoryId)")
    Long countProductsInCategoryAndChildren(@Param("categoryId") Long categoryId);
}
