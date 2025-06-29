package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.color.ColorCreateDTO;
import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.dto.admin.color.ColorUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

import java.util.List;

public interface ColorService {

    List<ColorResponseDTO> findAllColors();

    ColorResponseDTO findColorById(Long colorId);

    ColorResponseDTO createColor(ColorCreateDTO colorCreateDTO);

    ColorResponseDTO updateColorById(ColorUpdateDTO colorUpdateDTO, Long colorId);
    ResponseMessageDTO deleteColorById(Long colorId);
}
