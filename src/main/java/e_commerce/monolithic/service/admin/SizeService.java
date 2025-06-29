package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.size.SizeCreateDTO;
import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.dto.admin.size.SizeUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;

import java.util.List;

public interface SizeService {

    List<SizeResponseDTO> getAllSize();

    SizeResponseDTO getSizeById(Long id);

    SizeResponseDTO create(SizeCreateDTO createDTO);

    SizeResponseDTO update(SizeUpdateDTO updateDTO, Long id);

    ResponseMessageDTO delete(Long id);
}
