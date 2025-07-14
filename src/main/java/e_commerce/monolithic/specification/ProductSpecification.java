package e_commerce.monolithic.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Component
public class ProductSpecification {

    public Specification<Product> findByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null || maxPrice != null || minQuantity != null || maxQuantity != null) {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants");
                if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(variantJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(variantJoin.get("price"), maxPrice));
                }
                if (minQuantity != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(variantJoin.get("quantity"), minQuantity));
                }
                if (maxQuantity != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(variantJoin.get("quantity"), maxQuantity));
                }
                query.distinct(true);
            }

            // tìm theo tên
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // tìm theo trạng thái
            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), enabled));
            }

            // tìm theo danh mục
            if (categoryId != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }
            // tìm theo ngày tạo
            if (createdAt != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), createdAt.atStartOfDay(),
                        createdAt.atTime(23, 59, 59)));
            }
            // tìm theo ngày cập nhật
            if (updatedAt != null) {
                predicates.add(criteriaBuilder.between(root.get("updatedAt"), updatedAt.atStartOfDay(),
                        updatedAt.atTime(23, 59, 59)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Find products by category including all child categories (for DROPDOWN type
     * categories)
     * This method is used when we want to show all products from a parent category
     * and its children
     */
    public Specification<Product> findByCategoryIncludingChildren(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean enabled, List<Long> categoryIds,
            LocalDate createdAt, LocalDate updatedAt) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // tìm theo tên
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // tìm theo trạng thái
            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), enabled));
            }

            // tìm theo danh mục (bao gồm children)
            if (categoryIds != null && !categoryIds.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(categoryJoin.get("id").in(categoryIds));
            }

            // tìm theo ngày tạo
            if (createdAt != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), createdAt.atStartOfDay(),
                        createdAt.atTime(23, 59, 59)));
            }

            // tìm theo ngày cập nhật
            if (updatedAt != null) {
                predicates.add(criteriaBuilder.between(root.get("updatedAt"), updatedAt.atStartOfDay(),
                        updatedAt.atTime(23, 59, 59)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Find products by exact category (for LINK type categories)
     * This method is used when we want to show products from only a specific
     * category
     */
    public Specification<Product> findByExactCategory(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt) {
        return findByCriteria(name, minPrice, maxPrice, null, null, enabled, categoryId, createdAt, updatedAt);
    }
}
