package e_commerce.monolithic.dto.user.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDTO {

    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    private Long paymentMethodId;
}
