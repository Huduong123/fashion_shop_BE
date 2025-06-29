package e_commerce.monolithic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sizes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
}
