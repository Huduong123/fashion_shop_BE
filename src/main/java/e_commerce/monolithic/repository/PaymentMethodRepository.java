package e_commerce.monolithic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.monolithic.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{
    

    // TÌm tất cả các phương thức thanh toán đang được kích hoạt
    List<PaymentMethod> findByEnabledTrue();

    Optional<PaymentMethod> findByCode(String code);
    Optional<PaymentMethod> findByName(String name);
}
