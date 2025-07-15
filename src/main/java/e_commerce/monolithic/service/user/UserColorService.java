package e_commerce.monolithic.service.user;

import java.util.List;

import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.entity.Color;

public interface UserColorService {
    List<ColorResponseDTO> getAllColors();
}
