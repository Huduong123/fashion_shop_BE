package e_commerce.monolithic.specification;

import e_commerce.monolithic.entity.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySpecification {

    public Specification<Category> findByCriteria(String name, LocalDate createdAt, LocalDate updatedAt) {
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

            return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
