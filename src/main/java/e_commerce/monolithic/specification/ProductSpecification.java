package e_commerce.monolithic.specification;

import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantSize; // Bổ sung import
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

    /**
     * Tạo Specification để tìm kiếm Product dựa trên nhiều tiêu chí.
     * Đã sửa lỗi lọc giá bằng cách join đến ProductVariantSize.
     */
    public Specification<Product> findByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ================== SỬA LỖI LỌC GIÁ ==================
            // Nếu có điều kiện lọc theo giá, ta cần join qua các bảng
            // Product -> ProductVariant -> ProductVariantSize
            if (minPrice != null || maxPrice != null) {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants");
                Join<ProductVariant, ProductVariantSize> variantSizeJoin = variantJoin.join("productVariantSizes");

                if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(variantSizeJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(variantSizeJoin.get("price"), maxPrice));
                }
                // Thêm distinct() để đảm bảo mỗi sản phẩm chỉ xuất hiện một lần
                // dù nó có nhiều size khớp với điều kiện giá.
                query.distinct(true);
            }
            // =======================================================

            // Lọc theo số lượng (cũng cần join đến ProductVariantSize)
            if (minQuantity != null || maxQuantity != null) {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants");
                Join<ProductVariant, ProductVariantSize> variantSizeJoin = variantJoin.join("productVariantSizes");
                if (minQuantity != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(variantSizeJoin.get("quantity"), minQuantity));
                }
                if (maxQuantity != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(variantSizeJoin.get("quantity"), maxQuantity));
                }
                query.distinct(true);
            }

            // tìm theo tên sản phẩm
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // tìm theo trạng thái (enabled)
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
     * Tìm sản phẩm theo danh mục, bao gồm cả các danh mục con.
     * Đã sửa lỗi lọc giá tương tự như findByCriteria.
     */
    public Specification<Product> findByCategoryIncludingChildren(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean enabled, List<Long> categoryIds,
            LocalDate createdAt, LocalDate updatedAt) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ================== SỬA LỖI LỌC GIÁ ==================
            if (minPrice != null || maxPrice != null) {
                Join<Product, ProductVariant> variantJoin = root.join("productVariants");
                Join<ProductVariant, ProductVariantSize> variantSizeJoin = variantJoin.join("productVariantSizes");
                if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(variantSizeJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(variantSizeJoin.get("price"), maxPrice));
                }
                query.distinct(true);
            }
            // =======================================================
            
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
     * Tìm sản phẩm theo một danh mục chính xác (cho danh mục loại LINK).
     * Phương thức này gọi lại findByCriteria đã được sửa lỗi.
     */
    public Specification<Product> findByExactCategory(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt) {
        return findByCriteria(name, minPrice, maxPrice, null, null, enabled, categoryId, createdAt, updatedAt);
    }
}