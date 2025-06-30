package e_commerce.monolithic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color extends  BaseEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
