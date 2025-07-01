package e_commerce.monolithic.specification;

import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification {


    public Specification<Product> findByCriteria(String name,
                                                 BigDecimal minPrice,
                                                 BigDecimal maxPrice,
                                                 Integer minQuantity,
                                                 Integer maxQuantity,
                                                 Boolean enabled,
                                                 Long categoryId,
                                                 LocalDate createdAt,
                                                 LocalDate updatedAt) {
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
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
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
                predicates.add(criteriaBuilder.between(root.get("createdAt"), createdAt.atStartOfDay(), createdAt.atTime(23,59,59)));
            }
            if (updatedAt != null) {
                predicates.add(criteriaBuilder.between(root.get("updatedAt"), updatedAt.atStartOfDay(), updatedAt.atTime(23,59,59)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
