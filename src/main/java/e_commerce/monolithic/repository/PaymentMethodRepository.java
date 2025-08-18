package e_commerce.monolithic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.monolithic.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{
    
}
