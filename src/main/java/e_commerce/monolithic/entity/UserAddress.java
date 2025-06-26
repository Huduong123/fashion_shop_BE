package e_commerce.monolithic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString (exclude = "user")
public class UserAddress extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recipient_name", length = 100, nullable = false)
    private String recipientName;

    @Column(name = "phone_number",length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "address_detail", columnDefinition = "TEXT",nullable = false)
    private String addressDetail;

    @Column(name = "is_default")
    private boolean isDefault;
}
