package e_commerce.monolithic.service.user;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.user.payment_method.PaymentMethodDTO;
import e_commerce.monolithic.mapper.PaymentMethodMapper;
import e_commerce.monolithic.repository.PaymentMethodRepository;

@Service
public class UserPaymentMethodServiceImp implements UserPaymentMethodService{

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    

    public UserPaymentMethodServiceImp(PaymentMethodRepository paymentMethodRepository,
            PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }



    @Override
    public List<PaymentMethodDTO> getActivePaymentMethods() {
       return paymentMethodRepository.findByEnabledTrue()
                .stream()
                .map(paymentMethodMapper::convertToPaymentMethodDTO)
                .collect(Collectors.toList());
    
    }
    
}
