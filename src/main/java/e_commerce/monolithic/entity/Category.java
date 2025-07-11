package e_commerce.monolithic.entity;

import e_commerce.monolithic.entity.enums.CategoryStatus;
import e_commerce.monolithic.entity.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "products", "children", "parent" })
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Builder.Default
    private CategoryType type = CategoryType.DROPDOWN;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CategoryStatus status = CategoryStatus.ACTIVE;

    // Parent category - Many categories can have one parent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Child categories - One category can have many children
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // Helper methods for managing parent-child relationships
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    // Check if this category is a root category (has no parent)
    public boolean isRoot() {
        return parent == null;
    }

    // Check if this category is a leaf category (has no children)
    public boolean isLeaf() {
        return children.isEmpty();
    }

    // Check if this category can contain products
    public boolean canContainProducts() {
        return type == CategoryType.LINK;
    }

    // Check if this category can contain children categories
    public boolean canContainChildren() {
        return type == CategoryType.DROPDOWN;
    }

    // Check if this category is active
    public boolean isActive() {
        return status == CategoryStatus.ACTIVE;
    }

    // Check if this category is inactive
    public boolean isInactive() {
        return status == CategoryStatus.INACTIVE;
    }
}
