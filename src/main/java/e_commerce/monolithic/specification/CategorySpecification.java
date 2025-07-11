package e_commerce.monolithic.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.enums.CategoryType;
import jakarta.persistence.criteria.Predicate;

@Component
public class CategorySpecification {

    public Specification<Category> findByCriteria(String name, LocalDate createdAt, LocalDate updatedAt) {
        return findByCriteria(name, createdAt, updatedAt, null, null, null);
    }

    public Specification<Category> findByCriteria(String name, LocalDate createdAt, LocalDate updatedAt, 
                                                 Long parentId, Boolean isRoot) {
        return findByCriteria(name, createdAt, updatedAt, parentId, isRoot, null);
    }

    public Specification<Category> findByCriteria(String name, LocalDate createdAt, LocalDate updatedAt, 
                                                 Long parentId, Boolean isRoot, CategoryType type) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // tìm theo tên
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // tìm theo ngày tạo
            if (createdAt != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), createdAt.atStartOfDay(), createdAt.atTime(23,59,59)));
            }
            
            // tìm theo ngày cập nhật
            if (updatedAt != null) {
                predicates.add(criteriaBuilder.between(root.get("updatedAt"), updatedAt.atStartOfDay(), updatedAt.atTime(23,59,59)));
            }

            // tìm theo parent category
            if (parentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("parent").get("id"), parentId));
            }

            // tìm root categories (không có parent) hoặc categories có parent
            if (isRoot != null) {
                if (isRoot) {
                    predicates.add(criteriaBuilder.isNull(root.get("parent")));
                } else {
                    predicates.add(criteriaBuilder.isNotNull(root.get("parent")));
                }
            }

            // tìm theo type
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Specification để tìm tất cả children của một category (including nested)
    public Specification<Category> findAllChildren(Long parentId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("parent").get("id"), parentId);
        };
    }

    // Specification để tìm root categories
    public Specification<Category> findRootCategories() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get("parent"));
        };
    }

    // Specification để tìm categories theo level
    public Specification<Category> findByLevel(Integer level) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (level == null) {
                return criteriaBuilder.conjunction();
            }

            if (level == 0) {
                // Root level - no parent
                return criteriaBuilder.isNull(root.get("parent"));
            } else {
                // For now, we can only efficiently query direct children (level 1)
                // For deeper levels, would need recursive queries or application logic
                return criteriaBuilder.isNotNull(root.get("parent"));
            }
        };
    }
}
