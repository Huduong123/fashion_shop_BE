package e_commerce.monolithic.service.user;

import java.util.List;

import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;

public interface UserSizeService {
    
    List<SizeResponseDTO> getAllSizes();
}
